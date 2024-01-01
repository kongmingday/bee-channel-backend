package com.beechannel.base.constant;

import lombok.Getter;

/**
 * @Description common constants
 * @Author eotouch
 * @Date 2023/12/08 14:52
 * @Version 1.0
 */
@Getter
public enum CommonConstant {
    NO_ONLINE(1, "please login firstly", "no login"),
    SAME_USER(2, "don't act to yourself", "processing users is same"),

    NO_AUTHENTICATION(3, "you don't have access to this data", "no authentication");

    private final Integer code;
    private final String message;
    private final String reason;

    CommonConstant(Integer code, String message, String reason) {
        this.code = code;
        this.message = message;
        this.reason = reason;
    }
}
