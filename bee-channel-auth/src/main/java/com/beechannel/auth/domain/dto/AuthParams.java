package com.beechannel.auth.domain.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eotouch
 * @version 1.0
 * @description 认证用户请求参数
 * @date 2022/9/29 10:56
 */
@Data
public class AuthParams {

    private String username;
    private String unionId; // WeChat unionId
    private String password;
    private String email;
    private String checkCode;// verify code
    private String checkCodeKey;// verify key
    private String authType; // authentication type
    private Map<String, Object> payload = new HashMap<>();// extend message


}
