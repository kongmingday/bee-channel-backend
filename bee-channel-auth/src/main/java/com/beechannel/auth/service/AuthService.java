package com.beechannel.auth.service;

import com.beechannel.auth.domain.dto.AuthParams;
import com.beechannel.auth.domain.dto.UserExt;

/**
 * @Description unified authentication interface
 * @Author eotouch
 * @Date 2023/11/18 21:29
 * @Version 1.0
 */
public interface AuthService {

    /**
     * @description authentication method
     * @param authParams authentication params
     * @return com.beechannel.auth.domain.dto.UserExt
     * @author eotouch
     * @date 2023-11-18 21:32
     */
    UserExt execute(AuthParams authParams);
}
