package com.beechannel.auth.domain.dto;

import lombok.Data;

/**
 * @Description user sign up params
 * @Author eotouch
 * @Date 2023/11/24 18:56
 * @Version 1.0
 */
@Data
public class SignUpParams {

    private String key;
    private String code;
    private String email;
    private String password;
}
