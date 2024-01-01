package com.beechannel.user.feign;

import com.beechannel.base.config.FeignRequestInterceptor;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.user.feign.factory.FileUploadClientFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * @Description TODO
 * @Author eotouch
 * @Date 2023/12/20 23:35
 * @Version 1.0
 */
@FeignClient(
        value = "media-service",
        fallbackFactory = FileUploadClientFactory.class,
        configuration = FeignRequestInterceptor.class
)
@RequestMapping("/media/file")
public interface FileUploadClient {

    @PostMapping(value = "/upload/single", consumes = MULTIPART_FORM_DATA_VALUE)
    RestResponse<String> singleUploadFile(@RequestPart("file") MultipartFile file);
}
