package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.domain.dto.FileChunk;
import com.beechannel.media.domain.dto.FileUploadResult;
import com.beechannel.media.domain.po.MediaFile;
import com.beechannel.media.mapper.MediaFileMapper;
import com.beechannel.media.service.FileUploadService;
import com.beechannel.media.util.FileUrlUtil;
import io.minio.*;
import io.minio.messages.Item;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private FileUploadService fileUploadService;

    @Resource
    private MediaFileMapper mediaFileMapper;

    /**
     * @param file
     * @return com.beechannel.base.domain.vo.RestResponse
     * @description single file upload
     * @author eotouch
     * @date 2023-12-18 23:40
     */
    @Override
    @Transactional
    public RestResponse<FileUploadResult> singleUpload(MultipartFile file) {

        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();

        // check the file
        if (file == null) {
            return RestResponse.validFail("The file is empty.");
        }

        String result = null;
        try {
            // upload to minio
            result = fileUploadService.fileUploadToMinio(file.getBytes(), file.getOriginalFilename(), true);
        } catch (Exception e) {
            BeeChannelException.cast("The file upload error.");
        }

        // insert the file information to db
        MediaFile mediaFile = fileUploadToDB(currentUserId, result);
        if (mediaFile.getId() == null) {
            fileDeleteInMinio(result);
            BeeChannelException.cast("The file upload error.");
        }
        result = "/" + bucketName + result;

        FileUploadResult fileUploadResult = new FileUploadResult();
        fileUploadResult.setFileId(mediaFile.getId());
        fileUploadResult.setFilePath(result);
        return RestResponse.success(fileUploadResult);
    }

    /**
     * @param fileChunk file chunk
     * @return java.lang.Boolean
     * @description file chunk upload
     * @author eotouch
     * @date 2024-01-02 15:26
     */
    @Override
    public RestResponse chunkUpload(FileChunk fileChunk) {
        SecurityUtil.getCurrentUserIdNotNull();

        MultipartFile file = fileChunk.getChunk();
        String fileHash = fileChunk.getFileHash();
        String index = fileChunk.getIndex();

        // check the file
        if (file == null) {
            return RestResponse.validFail("The file is empty.");
        }

        byte[] fileBytes = null;
        try {
            fileBytes = file.getBytes();
        } catch (Exception e) {
            BeeChannelException.cast("The file upload has error");
        }
        String mimeType = FileUrlUtil.getMimeTypeByExtension(fileHash);
        String objectName = FileUrlUtil.generateChunkNameContainFolder(fileHash, mediaFolder) + "/" + index;
        fileUploadToMinio(fileBytes, objectName, mimeType);
        return RestResponse.success();
    }

    /**
     * @param fileHash   the file md5 hash
     * @param chunkCount the count of all chunk
     * @return com.beechannel.base.domain.vo.RestResponse<java.lang.String>
     * @description merge all chunk by the file hash
     * @author eotouch
     * @date 2024-01-02 22:47
     */
    @Override
    @Transactional
    public RestResponse<FileUploadResult> chunkMerge(String fileHash, int chunkCount) {
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();
        String result = null;
        String fileName = fileHash + ".mp4";

        // download all chunk
        List<File> fileList = downloadChunks(fileHash, chunkCount);

        // create temp file
        File tempFile = null;
        try {
            tempFile = File.createTempFile(fileHash, ".mp4");
        } catch (Exception e) {
            BeeChannelException.cast("the file chunk merge has error");
        }

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(tempFile, "rw")) {
            // merge chunk
            byte[] bytes = new byte[1024];
            for (File file : fileList) {
                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    int len = -1;
                    while ((len = fileInputStream.read(bytes)) != - 1) {
                        randomAccessFile.write(bytes, 0, len);
                    }
                }
            }

            // upload to minio
            result = fileUploadService.fileUploadToMinio(Files.readAllBytes(tempFile.toPath()), fileName, false);
        } catch (Exception e) {
            BeeChannelException.cast("the file chunk merge has error");
        }

        // insert the file information to db
        MediaFile mediaFile = fileUploadService.fileUploadToDB(currentUserId, result);

        // remove chunk in minio
        String chunkFolder = FileUrlUtil.generateChunkNameContainFolder(fileHash, mediaFolder) + "/";
        IntStream.rangeClosed(0, chunkCount - 1)
                .mapToObj(index -> chunkFolder + index)
                .forEach(this::fileDeleteInMinio);

        FileUploadResult fileUploadResult = new FileUploadResult();
        fileUploadResult.setFileId(mediaFile.getId());
        fileUploadResult.setFilePath("/" + bucketName + result);
        return RestResponse.success(fileUploadResult);
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
    public String fileUploadToMinio(byte[] fileByte, String fileName, boolean requireMd5Cal) {

        // get the file's mime type, then set upload args
        String fileMd5;
        if (requireMd5Cal) {
            fileMd5 = DigestUtils.md5DigestAsHex(fileByte);
        } else {
            fileMd5 = fileName.substring(0, fileName.lastIndexOf("."));
        }

        String extension = fileName.substring(fileName.lastIndexOf("."));
        String mimeType = FileUrlUtil.getMimeTypeByExtension(extension);
        String objectTemplate = "/%s/" + FileUrlUtil.generateObjectNameContainFolderBySplit(fileMd5, extension);
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

        // check file exist before upload
        boolean fileExisted = checkFile(objectName);
        if (fileExisted) {
            return objectName;
        }

        // start upload
        fileUploadToMinio(fileByte, objectName, mimeType);

        // get whether the file exists
        fileExisted = checkFile(objectName);
        if (Boolean.FALSE.equals(fileExisted)) {
            BeeChannelException.cast("The file upload error.");
        }

        return objectName;
    }

    /**
     * @param fileByte   the file bytes
     * @param objectName the file name
     * @param mimeType   the mime type
     * @return void
     * @description file upload to minio
     * @author eotouch
     * @date 2024-01-02 22:14
     */
    @Override
    public void fileUploadToMinio(byte[] fileByte, String objectName, String mimeType) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileByte)) {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .contentType(mimeType)
                    .stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
        } catch (Exception e) {
            BeeChannelException.cast("The file upload has error");
        }
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
    public MediaFile fileUploadToDB(Long currentUserId, String filePath) {
        LambdaQueryWrapper<MediaFile> fileQuery = new LambdaQueryWrapper<>();
        fileQuery.eq(MediaFile::getFilePath, filePath);
        fileQuery.eq(MediaFile::getBucket, bucketName);
        fileQuery.eq(MediaFile::getUploadUser, currentUserId);
        MediaFile existedFile = mediaFileMapper.selectOne(fileQuery);
        if (existedFile != null) {
            return existedFile;
        }

        MediaFile mediaFile = new MediaFile();
        mediaFile.setUploadUser(currentUserId);
        mediaFile.setBucket(bucketName);
        mediaFile.setFilePath(filePath);
        mediaFile.setUploadTime(LocalDateTime.now());
        mediaFileMapper.insert(mediaFile);
        return mediaFile;
    }

    /**
     * @param filePath the file path (file or folder)
     * @return void
     * @description delete file in minio
     * @author eotouch
     * @date 2024-01-02 14:51
     */
    @Override
    public void fileDeleteInMinio(String filePath) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            BeeChannelException.cast(e.getMessage());
        }
    }

    /**
     * @param objectName the object name
     * @return java.lang.Boolean
     * @description check file in minio
     * @author eotouch
     * @date 2024-01-02 14:52
     */
    @Override
    public Boolean checkFile(String objectName) {
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(objectName).build();
        Iterable<Result<Item>> target = minioClient.listObjects(listObjectsArgs);
        return target.iterator().hasNext();
    }

    /**
     * @param chunkFolder the chunk folder
     * @return java.lang.Boolean
     * @description check file in minio
     * @author eotouch
     * @date 2024-01-02 14:52
     */
    @Override
    public Boolean checkChunk(String chunkFolder, int length) {
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(chunkFolder).build();
        Iterable<Result<Item>> target = minioClient.listObjects(listObjectsArgs);
        Iterator<Result<Item>> iterator = target.iterator();
        int fileCount = 0;
        while (iterator.hasNext()) {
            fileCount++;
            iterator.next();
        }
        return fileCount == length;
    }

    /**
     * @description check whether the file exists before upload
     * @param fileHash the file md5 hash
     * @param extension the file suffix
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2024-01-04 15:43
     */
    @Override
    public RestResponse checkFileBeforeUpload(String fileHash, String extension) {
        String template = "/%s/%s";
        String filePath = FileUrlUtil.generateObjectNameContainFolderBySplit(fileHash, extension);
        String objectName = String.format(template, mediaFolder, filePath);
        Boolean result = fileUploadService.checkFile(objectName);

        if(result){
            Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();
            LambdaQueryWrapper<MediaFile> fileQuery = new LambdaQueryWrapper<>();
            fileQuery.eq(MediaFile::getFilePath, objectName);
            // fileQuery.eq(MediaFile::getUploadUser, currentUserId);
            MediaFile target = mediaFileMapper.selectOne(fileQuery);

            FileUploadResult fileUploadResult = new FileUploadResult();
            fileUploadResult.setFilePath("/" + bucketName + target.getFilePath());
            fileUploadResult.setFileId(target.getId());
            return RestResponse.success(fileUploadResult);
        }
        return RestResponse.validFail();
    }

    /**
     * @param fileName the file name
     * @return java.io.File
     * @description download file from minio
     * @author eotouch
     * @date 2024-01-02 23:27
     */
    @Override
    public File fileDownloadFromMinio(String fileName) {
        File tempFile = null;
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();
        try (InputStream inputStream = minioClient.getObject(getObjectArgs)) {
            tempFile = File.createTempFile(fileName, "");
            OutputStream outputStream = Files.newOutputStream(tempFile.toPath());
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            BeeChannelException.cast("the file upload has error");
        }
        return tempFile;
    }

    /**
     * @param fileHash   the fish md5 hash
     * @param chunkCount the chunk count
     * @return java.util.List<java.io.File>
     * @description download all chunk by fileHash
     * @author eotouch
     * @date 2024-01-02 23:51
     */
    @Override
    public List<File> downloadChunks(String fileHash, int chunkCount) {
        // check the count of all chunk
        String chunkFolder = FileUrlUtil.generateChunkNameContainFolder(fileHash, mediaFolder) + "/";
        boolean checkChunk = checkChunk(chunkFolder, chunkCount);
        if (!checkChunk) {
            BeeChannelException.cast("the file upload has error");
        }

        return IntStream.rangeClosed(0, chunkCount - 1)
                .mapToObj(index -> chunkFolder + index)
                .map(this::fileDownloadFromMinio)
                .collect(Collectors.toList());
    }

}
