package com.beechannel.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.auth.domain.dto.AuthParams;
import com.beechannel.auth.domain.dto.UserExt;
import com.beechannel.auth.mapper.UserMapper;
import com.beechannel.auth.service.AuthService;
import com.beechannel.base.domain.dto.UserDetail;
import com.beechannel.base.domain.po.Role;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.exception.BeeChannelException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Description Load username and password from db
 * @Author eotouch
 * @Date 2023/11/14 22:26
 * @Version 1.0
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private ApplicationContext applicationContext;

    // Load user's message from params.
    @Override
    public UserDetails loadUserByUsername(String userMessage) throws UsernameNotFoundException {

        // Exchange and encapsulate data from userMessage.
        AuthParams authParams = null;
        try {
            authParams = JSON.parseObject(userMessage, AuthParams.class);
        }catch (Exception e){
            BeeChannelException.cast("认证参数不符和要求");
        }

        // Get authentication bean and authorize by type.
        String authType = authParams.getAuthType();
        String beanName = authType + "AuthService";

        AuthService authService = applicationContext.getBean(beanName, AuthService.class);
        UserExt authUser = authService.execute(authParams);

        UserDetails userDetails = processResult(authUser);
        return userDetails;
    }


    /**
     * @description process the authentication result to return security
     * @param userExt authentication result
     * @return org.springframework.security.core.userdetails.UserDetails
     * @author eotouch
     * @date 2023-11-18 22:34
     */
    private UserDetails processResult(UserExt userExt){

        User user = new User();
        UserDetail userDetail = new UserDetail();

        BeanUtils.copyProperties(userExt, user);
        userDetail.setUser(user);
        List<Role> roleList = userExt.getRoleList();
        userDetail.setRoleList(roleList);

        return userDetail;
    }

}
