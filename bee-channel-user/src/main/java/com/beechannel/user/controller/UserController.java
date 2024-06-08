package com.beechannel.user.controller;

import com.beechannel.base.constant.CommonConstant;
import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.domain.vo.SearchParams;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.user.service.ConcernService;
import com.beechannel.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
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

    @Resource
    private PasswordEncoder passwordEncoder;

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

    @GetMapping("/full/page")
    public RestResponse<FullUser> getFullUserInfoList(SearchParams searchParams,
                                                  @RequestParam(value = "currentId", required = false) Long currentId) {
        return userService.getFullUserInfoList(searchParams, currentId);
    }

    @GetMapping("/subscription")
    public RestResponse getSubscriptionPage(SearchParams searchParams){
        return userService.getSubscriptionPage(searchParams);
    }

    @GetMapping("/subscription/all")
    public RestResponse getAllSubscription(){
        return userService.getAllSubscription();
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
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();

        user.setId(currentUserId);
        String password = user.getNewPassword();
        if(StringUtils.hasText(password)){
            password = passwordEncoder.encode(password);
            user.setPassword(password);
        }
        boolean result = userService.updateById(user);

        return result ? RestResponse.success(user) : RestResponse.validFail();
    }

}
