package com.beechannel.base.util;

import com.alibaba.fastjson.JSON;
import com.beechannel.base.domain.dto.UserDetail;
import com.beechannel.base.domain.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
        try {
            Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principalObj instanceof String) {
                //取出用户身份信息
                String principal = principalObj.toString();
                //将json转成对象
                User user = JSON.parseObject(principal, User.class);
                return user;
            }
        } catch (Exception e) {
            log.error("get current user's message fail:{}", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
