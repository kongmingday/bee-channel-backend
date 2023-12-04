package com.beechannel.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.auth.domain.dto.AuthParams;
import com.beechannel.auth.domain.dto.UserExt;
import com.beechannel.auth.domain.po.UserRole;
import com.beechannel.auth.mapper.UserMapper;
import com.beechannel.auth.mapper.UserRoleMapper;
import com.beechannel.auth.service.WeChatAuthService;
import com.beechannel.base.constant.UserRoleType;
import com.beechannel.base.constant.UserStatus;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.exception.BeeChannelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description WeChat authentication
 * @Author eotouch
 * @Date 2023/11/18 21:35
 * @Version 1.0
 */
@Service("WeChatAuthService")
@Slf4j
public class WeChatAuthServiceImpl implements WeChatAuthService {


    @Value("${bee-channel.wechat.secret}")
    private String secret;
    @Value("${bee-channel.wechat.appid}")
    private String appid;

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private WeChatAuthServiceImpl weChatAuthService;

    /**
     * @description WeChat login
     * @param authParams authentication params
     * @return com.beechannel.auth.domain.dto.UserExt
     * @author eotouch
     * @date 2023-11-18 21:33
     */
    @Override
    public UserExt execute(AuthParams authParams) {

        String unionId = authParams.getUnionId();
        UserExt userExt = userMapper.getUserAndRoleList(unionId);
        if(userExt == null){
            BeeChannelException.cast("user not exist");
        }

        return userExt;
    }

    /**
     * @description WeChat's authentication service
     * @param code the grant code from WeChat
     * @return com.beechannel.base.domain.po.User
     * @author eotouch
     * @date 2023-11-20 16:22
     */
    @Override
    public User wechatAuth(String code) {
        // get accessToken
        Map<String, String> tokenMap = getAccessToken(code);

        // get user's information
        String openId = tokenMap.get("openid");
        String accessToken = tokenMap.get("access_token");
        Map<String, String> userInfo = getUserinfo(accessToken, openId);

        // insert to db
        User user = weChatAuthService.addWeChatUser(userInfo);
        return user;
    }

    /**
     * @description insert user to db by WeChat's information
     * @param userInfo
     * @return com.beechannel.auth.domain.dto.UserExt
     * @author eotouch
     * @date 2023-11-20 15:49
     */
    @Transactional
    public User addWeChatUser(Map userInfo){

        // get user's message by unionId from db
        String unionid = userInfo.get("unionid").toString();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getWxUnionId, unionid));
        if(user != null){
            return user;
        }

        // set user's message and insert
        user = new User();
        user.setWxUnionId(unionid);
        user.setProfile(userInfo.get("headimgurl").toString());
        user.setUsername(userInfo.get("nickname").toString());
        user.setPassword(unionid);
        user.setStatus(UserStatus.ENABLE.getCode());
        user.setCreateTime(LocalDateTime.now());
        int insert = userMapper.insert(user);
        log.info(String.valueOf(insert));

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(UserRoleType.COMMON_USER.getId());
        userRole.setCreateTime(LocalDateTime.now());
        userRoleMapper.insert(userRole);
        return user;
    }

    /**
     * @description get access_token
     * @param code authentication code
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author eotouch
     * @date 2023-03-17 20:52
     */
    private Map<String,String> getAccessToken(String code){
        // url fill
        String urlTemplate = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        String url = String.format(urlTemplate, appid, secret, code);
        // remote process call
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        String body = exchange.getBody();
        return JSON.parseObject(body, Map.class);
    }

    /**
     * @description get user's information
     * @param access_token
     * @param openid
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author eotouch
     * @date 2023-03-17 20:53
     */
    private Map<String,String> getUserinfo(String access_token,String openid){
        String urlTemplate = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
        String url = String.format(urlTemplate, access_token, openid);

        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        String result = new String(exchange.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return JSON.parseObject(result, Map.class);
    }
}
