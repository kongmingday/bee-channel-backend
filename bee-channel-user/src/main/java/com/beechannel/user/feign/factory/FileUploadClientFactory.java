package com.beechannel.user.feign.factory;

import com.beechannel.base.constant.InnerRpcStatus;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.feign.AbstractFeignFactory;
import com.beechannel.user.feign.FileUploadClient;
import feign.hystrix.FallbackFactory;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description TODO
 * @Author eotouch
 * @Date 2023/12/20 23:36
 * @Version 1.0
 */
@Component
@Slf4j
public class FileUploadClientFactory  implements FallbackFactory<FileUploadClient> {

    @Override
    public FileUploadClient create(Throwable cause) {
        return null;
    }

    @Setter
    @NoArgsConstructor
    public class UserClientFactoryImpl extends AbstractFeignFactory implements FileUploadClient{

        private Throwable throwable;

        @Override
        public RestResponse<String> singleUploadFile(MultipartFile file) {
            super.rpcFail(throwable);
            return RestResponse.validFail(InnerRpcStatus.ERROR.getDescription());
        }
    }
}
