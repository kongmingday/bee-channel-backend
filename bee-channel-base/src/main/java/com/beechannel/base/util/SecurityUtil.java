package com.beechannel.base.util;

import com.alibaba.fastjson.JSON;
import com.beechannel.base.constant.CommonConstant;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.exception.BeeChannelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.validation.constraints.NotNull;
import java.security.Principal;

/**
 * @Description Security Util
 * @Author eotouch
 * @Date 2023/11/16 0:23
 * @Version 1.0
 */
@Slf4j
public class SecurityUtil {

    /**
     * @description get user's message from SecurityContext
     * @return com.beechannel.base.domain.dto.UserDetail
     * @author eotouch
     * @date 2023-11-16 1:17
     */
    public static User getCurrentUser(){
        User user = null;
        try {
            Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principalObj instanceof String) {
                //取出用户身份信息
                String principal = principalObj.toString();
                //将json转成对象
                user = JSON.parseObject(principal, User.class);
            }
        } catch (Exception e) {
            log.error("get current user's message fail:{}", e.getMessage());
        }finally {
            return user;
        }
    }

    /**
     * @description get user's id from SecurityContext
     * @return com.beechannel.base.domain.dto.UserDetail
     * @author eotouch
     * @date 2023-11-16 1:17
     */
    public static Long getCurrentUserId(){
        try {
            User currentUser = getCurrentUser();
            if(currentUser != null){
                return currentUser.getId();
            }
        } catch (Exception e) {
            log.error("get current user's message fail:{}", e.getMessage());
        }
        return null;
    }

    /**
     * @description get user's id from SecurityContext
     * @return com.beechannel.base.domain.dto.UserDetail
     * @author eotouch
     * @date 2023-11-16 1:17
     */
    public static Long getCurrentUserId(User currentUser){
        try {
            if(currentUser != null){
                return currentUser.getId();
            }
        } catch (Exception e) {
            log.error("get current user's message fail:{}", e.getMessage());
        }
        return null;
    }

    /**
     * @description check current user and get user id`
     * @return java.lang.Long
     * @author eotouch
     * @date 2023-12-10 18:24
     */
    @NotNull
    public static Long getCurrentUserIdNotNull() {
        Long currentUserId = getCurrentUserId();
        if(currentUserId == null){
            BeeChannelException.cast(CommonConstant.NO_ONLINE.getMessage());
        }
        return currentUserId;
    }

    /**
     * @description get user's message from userPrincipal
     * @param userPrincipal the user principal
     * @return com.beechannel.base.domain.po.User
     * @author eotouch
     * @date 2024-01-06 15:35
     */
    public static User getCurrentUser(Principal userPrincipal){
        User user = null;
        try {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) userPrincipal;
            Object principalObj = oAuth2Authentication.getUserAuthentication().getPrincipal();
            if (principalObj instanceof String) {
                //取出用户身份信息
                String principal = principalObj.toString();
                //将json转成对象
                user = JSON.parseObject(principal, User.class);
            }
        } catch (Exception e) {
            log.error("get current user's message fail:{}", e.getMessage());
        }finally {
            return user;
        }
    }

    /**
     * @description check current user and get user id`
     * @return java.lang.Long
     * @author eotouch
     * @date 2024-01-06 15:35
     */
    @NotNull
    public static Long getCurrentUserIdNotNull(Principal userPrincipal) {
        Long currentUserId = getCurrentUserId(getCurrentUser(userPrincipal));
        if(currentUserId == null){
            BeeChannelException.cast(CommonConstant.NO_ONLINE.getMessage());
        }
        return currentUserId;
    }

    /**
     * @description check current user and get user id`
     * @return java.lang.Long
     * @author eotouch
     * @date 2024-01-06 15:35
     */
    @NotNull
    public static Long getCurrentUserId(Principal userPrincipal) {
        return getCurrentUserId(getCurrentUser(userPrincipal));
    }
}
