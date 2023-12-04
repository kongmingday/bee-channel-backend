package com.beechannel.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.auth.domain.dto.FullUser;
import com.beechannel.auth.domain.po.Concern;
import com.beechannel.auth.service.ConcernService;
import com.beechannel.auth.service.UserService;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description user service
 * @Author eotouch
 * @Date 2023/11/29 19:42
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @GetMapping("/{userId}")
    public RestResponse<User> getUserInfo(@PathVariable Long userId) {
        User user = userService.getById(userId);
        return RestResponse.success(user);
    }

    @GetMapping("/full/{userId}")
    public RestResponse<FullUser> getFullUserInfo(@PathVariable Long userId,
                                              @RequestParam(value = "currentId", required = false) Long currentId) {
        FullUser fullUser = userService.getFullInfoById(userId, currentId);
        return RestResponse.success(fullUser);
    }

}
