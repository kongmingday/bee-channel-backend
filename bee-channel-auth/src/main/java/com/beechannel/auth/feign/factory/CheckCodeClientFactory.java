package com.beechannel.auth.feign.factory;

import com.beechannel.auth.feign.CheckCodeClient;
import com.beechannel.base.feign.AbstractFeignFactory;
import feign.hystrix.FallbackFactory;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author eotouch
 * @version 1.0
 * @description feign client ruler
 * @date 2022/10/20 15:52
 */
@Slf4j
@Component
public class CheckCodeClientFactory implements FallbackFactory<CheckCodeClient> {
    @Override
    public CheckCodeClient create(Throwable throwable) {
        return new CheckCodeClientFactoryImpl();
    }

    @Setter
    @NoArgsConstructor
    public class CheckCodeClientFactoryImpl extends AbstractFeignFactory implements CheckCodeClient{

        private Throwable throwable;

        @Override
        public void enableAccount(String email) {
            super.rpcFail(throwable);
        }
    }

}
