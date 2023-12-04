package com.beechannel.media.feign.factory;

import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.feign.AbstractFeignFactory;
import com.beechannel.media.domain.dto.FullUser;
import com.beechannel.media.feign.UserClient;
import feign.hystrix.FallbackFactory;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author eotouch
 * @version 1.0
 * @description user client ruler
 * @date 2022/10/20 15:52
 */
@Slf4j
@Component
public class UserClientFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
        return new UserClientFactoryImpl();
    }

    @Setter
    @NoArgsConstructor
    public class UserClientFactoryImpl extends AbstractFeignFactory implements UserClient{

        private Throwable throwable;

        @Override
        public RestResponse<User> getUserInfo(Long userId) {
            super.rpcFail(throwable);
            return RestResponse.validFail("rpc error");
        }

        @Override
        public RestResponse<FullUser> getFullUserInfo(Long userId, Long currentId) {
            super.rpcFail(throwable);
            return RestResponse.validFail("rpc error");
        }
    }

}
