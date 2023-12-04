package com.beechannel.base.feign;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description default feign factory class
 * @Author eotouch
 * @Date 2023/11/24 17:42
 * @Version 1.0
 */
@Slf4j
public class AbstractFeignFactory {

    public void rpcFail(Throwable throwable) {
        log.debug("调用验证码服务熔断异常:{}", throwable.getMessage());
    }
}
