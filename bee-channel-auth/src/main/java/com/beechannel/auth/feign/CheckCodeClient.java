package com.beechannel.auth.feign;

import com.beechannel.auth.feign.factory.CheckCodeClientFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author eotouch
 * @version 1.0
 * @description rpc component
 * @date 2022/10/20 15:50
 */
@FeignClient(value = "checkcode-service", fallbackFactory = CheckCodeClientFactory.class)
@RequestMapping("/checkcode")
public interface CheckCodeClient {

    @PostMapping(value = "/enableAccount")
    void enableAccount(@RequestParam("email") String email);

}
