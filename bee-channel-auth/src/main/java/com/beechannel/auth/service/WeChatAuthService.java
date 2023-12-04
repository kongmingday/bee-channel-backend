package com.beechannel.auth.service;

import com.beechannel.base.domain.po.User;

/**
 * @Description TODO
 * @Author eotouch
 * @Date 2023/11/20 15:31
 * @Version 1.0
 */
public interface WeChatAuthService extends AuthService{

    /**
     * @description WeChat's authentication service
     * @param code the grant code from WeChat
     * @return User
     * @author eotouch
     * @date 2023-11-20 16:22
     */
    User wechatAuth(String code);
}
