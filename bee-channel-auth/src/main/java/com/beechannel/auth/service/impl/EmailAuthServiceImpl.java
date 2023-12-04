package com.beechannel.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.auth.domain.dto.AuthParams;
import com.beechannel.auth.domain.dto.UserExt;
import com.beechannel.auth.feign.CheckCodeClient;
import com.beechannel.auth.mapper.UserMapper;
import com.beechannel.auth.service.AuthService;
import com.beechannel.base.domain.po.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.beechannel.auth.constant.RedisStoreKey.SEND_KEY;

/**
 * @Description email authentication
 * @Author eotouch
 * @Date 2023/11/25 18:05
 * @Version 1.0
 */
@Service("EmailAuthService")
public class EmailAuthServiceImpl implements AuthService {
    @Resource
    private UserMapper userMapper;

    /**
     * @description email login
     * @param authParams authentication params
     * @return com.beechannel.auth.domain.dto.UserExt
     * @author eotouch
     * @date 2023/11/25 18:05
     */
    @Override
    public UserExt execute(AuthParams authParams) {

        UserExt userExt = userMapper.getUserAndRoleList(authParams.getEmail());
        return userExt;
    }
}
