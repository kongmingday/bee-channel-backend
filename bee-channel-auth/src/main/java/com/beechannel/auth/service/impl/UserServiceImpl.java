package com.beechannel.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.auth.domain.dto.SignUpParams;
import com.beechannel.auth.domain.po.UserRole;
import com.beechannel.auth.feign.CheckCodeClient;
import com.beechannel.auth.mapper.ConcernMapper;
import com.beechannel.auth.mapper.UserRoleMapper;
import com.beechannel.auth.service.UserService;
import com.beechannel.base.constant.UserRoleType;
import com.beechannel.base.constant.UserStatus;
import com.beechannel.base.domain.po.User;
import com.beechannel.auth.mapper.UserMapper;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.BeeChannelException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

import static com.beechannel.auth.constant.AuthSituation.*;

/**
* @author eotouch
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-11-14 22:42:59
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private CheckCodeClient checkCodeClient;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserService userService;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserMapper userMapper;


    /**
     * @description user register
     * @param params user's message
     * @return RestResponse result
     * @author eotouch
     * @date 2023-11-24 16:50
     */
    @Override
    @Transactional
    public RestResponse register(SignUpParams params) {

        // search this user from db
        String email = params.getEmail();
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(StringUtils.hasText(email), User::getEmail, email);
        User exist = userMapper.selectOne(userQuery);
        User user = new User();

        // when the user already exists, validating fail
        if(exist != null){
            if((int) UserStatus.ENABLE.getCode() == exist.getStatus()){
                return RestResponse.validFail(EXIST.getCode(), EXIST.getDescription());
            }else{
                user.setId(exist.getId());
            }
        }

        // insert user to db
        user.setUsername(params.getEmail());
        user.setEmail(params.getEmail());
        user.setPassword(passwordEncoder.encode(params.getPassword()));
        user.setStatus(UserStatus.DISABLE.getCode());
        user.setCreateTime(LocalDateTime.now());
        userService.saveOrUpdate(user);
        boolean userInsert = this.saveOrUpdate(user);

        // insert role to user
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(UserRoleType.COMMON_USER.getId());
        userRole.setCreateTime(LocalDateTime.now());
        boolean roleInsert = userRoleMapper.insert(userRole) > 0;

        if(!(userInsert && roleInsert)){
            BeeChannelException.cast("server client has error");
        }

        checkCodeClient.enableAccount(email);
        return RestResponse.success(SIGN_UP_SUCCESS.getDescription());
    }

    /**
     * @description enable user account
     * @param email email
     * @param token verify token
     * @return boolean
     * @author eotouch
     * @date 2023-11-24 23:10
     */
    @Override
    public boolean enableAccount(String email, String token) {
        User user = new User();
        user.setStatus(UserStatus.ENABLE.getCode());
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getEmail, email);
        boolean flag = userMapper.update(user, userQuery) > 0;
        return flag;
    }

}




