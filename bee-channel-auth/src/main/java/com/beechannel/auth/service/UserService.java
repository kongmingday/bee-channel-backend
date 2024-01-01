package com.beechannel.auth.service;

import com.beechannel.auth.domain.dto.SignUpParams;
import com.beechannel.base.domain.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beechannel.base.domain.vo.RestResponse;

/**
* @author eotouch
* @description 针对表【user】的数据库操作Service
* @createDate 2023-11-14 22:42:59
*/
public interface UserService extends IService<User> {

    /**
     * @description user register
     * @param params user's message
     * @return RestResponse result
     * @author eotouch
     * @date 2023-11-24 16:50
     */
    RestResponse register(SignUpParams params);

    /**
     * @description enable user account
     * @param email email
     * @param token verify token
     * @return boolean
     * @author eotouch
     * @date 2023-11-24 23:10
     */
    boolean enableAccount(String email, String token);

}
