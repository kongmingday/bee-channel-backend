package com.beechannel.media.service;

import com.beechannel.base.domain.vo.RestResponse;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Description file upload interface
 * @Author eotouch
 * @Date 2023/12/18 16:55
 * @Version 1.0
 */
public interface FileUploadService {

    RestResponse<String> singleUpload(MultipartFile file);

    RestResponse<Boolean> fileChunkUpload();

    String fileUploadToMinio(byte[] fileByte, String fileName, boolean requireMd5Cal) throws Exception;

    boolean fileUploadToDB(Long currentUserId, String filePath);

    void fileDeleteInMinio(String filePath) throws Exception;

    Boolean checkFile(String chunkName);
}
