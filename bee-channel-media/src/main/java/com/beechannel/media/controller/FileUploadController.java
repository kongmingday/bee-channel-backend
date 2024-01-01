package com.beechannel.media.controller;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.dto.FileChunk;
import com.beechannel.media.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * @Description file upload service
 * @Author eotouch
 * @Date 2023/12/18 16:43
 * @Version 1.0
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Resource
    private FileUploadService fileUploadService;

    @Value("${minio.bucket.video-folder}")
    private String mediaFolder;

    @PostMapping(value = "/upload/single", consumes = MULTIPART_FORM_DATA_VALUE)
    RestResponse<String> singleUploadFile(@RequestPart("file") MultipartFile file){
        return fileUploadService.singleUpload(file);
    }

    @GetMapping(value = "/upload/chunk/check")
    RestResponse<Boolean> checkChunk(String chunkName){
        String objectName = "/" + mediaFolder + "/" + chunkName;
        Boolean result = fileUploadService.checkFile(objectName);
        return RestResponse.success(result);
    }

    @PostMapping(value = "/upload/chunk", consumes = MULTIPART_FORM_DATA_VALUE)
    RestResponse<String> fileChunkUpload(FileChunk fileChunk){
        return RestResponse.success();
    }
}
