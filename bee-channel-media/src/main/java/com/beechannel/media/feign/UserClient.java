package com.beechannel.media.feign;

import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.feign.factory.UserClientFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author eotouch
 * @version 1.0
 * @description user rpc component
 * @date 2022/10/20 15:50
 */
@FeignClient(value = "user-service", fallbackFactory = UserClientFactory.class)
@RequestMapping("/user/info")
public interface UserClient {

    @GetMapping("/{userId}")
    RestResponse<User> getUserInfo(@PathVariable("userId") Long userId);

    @GetMapping("/full/{userId}")
    RestResponse<FullUser> getFullUserInfo(@PathVariable("userId") Long userId,
                                           @RequestParam(value = "currentId", required = false) Long currentId);

    @GetMapping("/batch")
    RestResponse<List<User>> getUserInfoByIdList(@RequestParam("idList") List<Long> idList);
}
