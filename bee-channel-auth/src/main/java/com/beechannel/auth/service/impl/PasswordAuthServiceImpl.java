package com.beechannel.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.auth.domain.dto.AuthParams;
import com.beechannel.auth.domain.dto.UserExt;
import com.beechannel.auth.feign.CheckCodeClient;
import com.beechannel.auth.mapper.UserMapper;
import com.beechannel.auth.service.AuthService;
import com.beechannel.base.domain.dto.UserDetail;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.exception.BeeChannelException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description password authentication
 * @Author eotouch
 * @Date 2023/11/18 21:33
 * @Version 1.0
 */
@Service("PasswordAuthService")
public class PasswordAuthServiceImpl implements AuthService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * @description password login
     * @param authParams authentication params
     * @return com.beechannel.auth.domain.dto.UserExt
     * @author eotouch
     * @date 2023-11-18 21:33
     */
    @Override
    public UserExt execute(AuthParams authParams) {

        String email = authParams.getEmail();
        String passwordFromUser = authParams.getPassword();

        // Load user's information from db, then match the password.
        UserExt userExt = userMapper.getUserAndRoleList(email);

        if(userExt == null){
            BeeChannelException.cast("user not exist");
        }

        String passwordFromDb = userExt.getPassword();
        boolean matches = passwordEncoder.matches(passwordFromUser, passwordFromDb);

        if(!matches){
            BeeChannelException.cast("user's password no match");
        }
        return userExt;
    }
}
