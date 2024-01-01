package com.beechannel.media.service.impl;

import com.beechannel.base.constant.CommonConstant;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.AcceptException;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.domain.po.MediaFile;
import com.beechannel.media.mapper.MediaFileMapper;
import com.beechannel.media.service.FileUploadService;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * @Description file upload implement
 * @Author eotouch
 * @Date 2023/12/18 16:55
 * @Version 1.0
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Value("${minio.bucket.image-folder}")
    private String imageFolder;

    @Value("${minio.bucket.video-folder}")
    private String mediaFolder;

    @Resource
    private MinioClient minioClient;

    @Resource
    private MediaFileMapper mediaFileMapper;

    private final int maxSingleSize = 5 * 1024 * 1024;

    /**
     * @param file
     * @return com.beechannel.base.domain.vo.RestResponse
     * @description single file upload
     * @author eotouch
     * @date 2023-12-18 23:40
     */
    @Override
    public RestResponse<String> singleUpload(MultipartFile file) {

        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            BeeChannelException.cast(CommonConstant.NO_ONLINE.getMessage());
        }

        // check the file
        if (file == null) {
            return RestResponse.validFail("The file is empty.");
        }

        String result = null;
        boolean insertDbResult = false;

        try {
            int fileSize = file.getBytes().length;
            if (fileSize >= maxSingleSize) {
                return RestResponse.validFail("The file length exceeded the maximum.");
            }

            result = fileUploadToMinio(file.getBytes(), file.getOriginalFilename(), true);

            insertDbResult = fileUploadToDB(currentUserId, result);

            if (!insertDbResult) {
                fileDeleteInMinio(result);
            }

        } catch (BeeChannelException e) {
            BeeChannelException.cast(e.getErrMessage());
        } catch (Exception e) {
            BeeChannelException.cast("The file upload error.");
        }

        result = "/" + bucketName  + result;

        return insertDbResult ? RestResponse.success(result, null) : RestResponse.validFail("The file upload error.");
    }

    @Override
    public RestResponse<Boolean> fileChunkUpload() {
        return null;
    }

    /**
     * @param fileByte the file's byte array
     * @param fileName the file's name
     * @return java.lang.String
     * @description upload file to minio
     * @author eotouch
     * @date 2023-12-18 23:33
     */
    @Override
    public String fileUploadToMinio(byte[] fileByte, String fileName, boolean requireMd5Cal) throws Exception {

        // get the file's mime type, then set upload args
        String fileMd5 = fileName;
        if(requireMd5Cal){
            fileMd5 = DigestUtils.md5DigestAsHex(fileByte);
        }
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String mimeType = getMimeTypeByExtension(extension);
        String objectTemplate = "/%s/" + generateFolderBySplit(fileMd5, extension);
        String targetFolder = "";

        // choose the start folder by mimeType
        String objectName = "";
        if (mimeType.contains("image")) {
            targetFolder = imageFolder;
        } else if (mimeType.contains("mp4")) {
            targetFolder = mediaFolder;
        } else {
            BeeChannelException.cast("The file type is incorrect");
        }
        objectName = String.format(objectTemplate, targetFolder);

        // start upload
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileByte);
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .contentType(mimeType)
                .stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                .build();
        minioClient.putObject(putObjectArgs);

        // get whether the file exists
        Boolean fileExisted = checkFile(objectName);
        if(!fileExisted){
            BeeChannelException.cast("The file upload error.");
        }

        return objectName;
    }

    /**
     * @param filePath      file path in minio
     * @param currentUserId current user id
     * @return boolean
     * @description upload file to db
     * @author eotouch
     * @date 2023-12-20 23:50
     */
    @Override
    public boolean fileUploadToDB(Long currentUserId, String filePath) {
        MediaFile mediaFile = new MediaFile();
        mediaFile.setUploadUser(currentUserId);
        mediaFile.setBucket(bucketName);
        mediaFile.setFilePath(filePath);
        mediaFile.setUploadTime(LocalDateTime.now());
        boolean result = mediaFileMapper.insert(mediaFile) > 0;
        return result;
    }

    @Override
    public void fileDeleteInMinio(String filePath) throws Exception{
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath)
                .build();
        minioClient.removeObject(removeObjectArgs);
    }

    @Override
    public Boolean checkFile(String objectName) {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName).build();
        try (InputStream stream = minioClient.getObject(getObjectArgs)){
            if (stream == null) {
                return false;
            }
        }catch (ErrorResponseException e){
            if("The specified key does not exist.".equals(e.getMessage())){
                AcceptException.cast(e.getMessage());
            }else{
                BeeChannelException.cast(e.getMessage());
            }
        }catch (Exception e) {
            BeeChannelException.cast(e.getMessage());
        }
        return true;
    }

    /**
     * @param fileMd5
     * @param extension
     * @return java.lang.String
     * @description generate folder name by fileMd5
     * @author eotouch
     * @date 2023-12-19 22:33
     */
    private String generateFolderBySplit(String fileMd5, String extension) {
        if (!StringUtils.hasText(fileMd5)) {
            BeeChannelException.cast("The file name is incorrect");
        }
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + extension;
    }


    /**
     * @param extension
     * @return java.lang.String
     * @description get the mime type by extension
     * @author eotouch
     * @date 2023-12-18 22:58
     */
    private String getMimeTypeByExtension(String extension) {

        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // set default mime type
        if (StringUtils.hasText(extension)) {
            ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
            if (extensionMatch != null) {
                contentType = extensionMatch.getMimeType();
            }
        }
        return contentType;
    }
}
