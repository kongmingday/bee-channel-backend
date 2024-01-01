package com.beechannel.user.controller;

import com.beechannel.base.constant.CommonConstant;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.user.domain.dto.FullUser;
import com.beechannel.user.service.ConcernService;
import com.beechannel.user.service.UserService;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description user service
 * @Author eotouch
 * @Date 2023/11/29 19:42
 * @Version 1.0
 */
@RestController
@RequestMapping("/info")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private ConcernService concernService;

    @GetMapping
    public RestResponse getUserInfo(){
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            BeeChannelException.cast(CommonConstant.NO_ONLINE.getMessage());
        }
        User cureentUser = userService.getById(currentUserId);
        return RestResponse.success(cureentUser);
    }

    @GetMapping("/batch")
    public RestResponse<List<User>> getUserInfoByIdList(@RequestParam("idList") List<Long> idList) {
        List<User> userList = userService.listByIds(idList);
        return RestResponse.success(userList);
    }

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

    @GetMapping("/subscribe/{userToId}")
    public RestResponse<Boolean> subscribeAction(@PathVariable Long userToId){
        return concernService.subscribeAction(userToId);
    }

    @PostMapping("/upload/avatar")
    public RestResponse updateAvatar(MultipartFile file){
        return userService.updateAvatar(file);
    }

    @PutMapping
    public RestResponse updateUserInfo(@RequestBody User user){
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            BeeChannelException.cast(CommonConstant.NO_ONLINE.getMessage());
        }

        user.setId(currentUserId);
        boolean result = userService.updateById(user);

        return result ? RestResponse.success(user) : RestResponse.validFail();
    }

}
