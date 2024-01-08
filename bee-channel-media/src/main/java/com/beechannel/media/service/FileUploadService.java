package com.beechannel.media.service;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.dto.FileChunk;
import com.beechannel.media.domain.dto.FileUploadResult;
import com.beechannel.media.domain.po.MediaFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * @Description file upload interface
 * @Author eotouch
 * @Date 2023/12/18 16:55
 * @Version 1.0
 */
public interface FileUploadService {

    RestResponse<FileUploadResult> singleUpload(MultipartFile file);

    RestResponse<String> chunkUpload(FileChunk fileChunk);

    RestResponse<FileUploadResult> chunkMerge(String fileHash, int chunkCount);

    String fileUploadToMinio(byte[] fileByte, String fileName, boolean requireMd5Cal);

    MediaFile fileUploadToDB(Long currentUserId, String filePath);

    void fileUploadToMinio(byte[] fileByte, String objectName, String mimeType);

    File fileDownloadFromMinio(String fileName);

    void fileDeleteInMinio(String filePath) ;

    Boolean checkFile(String chunkName);

    List<File> downloadChunks(String fileHash, int chunkCount);

    Boolean checkChunk(String chunkFolder, int length);

    RestResponse checkFileBeforeUpload(String fileHash, String extension);
}
