package com.beechannel.auth.constant;

import lombok.Getter;

/**
 * @Description sign up situation
 * @Author eotouch
 * @Date 2023/11/24 19:03
 * @Version 1.0
 */
@Getter
public enum AuthSituation {

    EXIST(1001, "Your email already exists"),
    VERIFY_FAIL(1002, "Your verified code is illegal"),
    INCONSISTENCY(1003, "Password inconsistency"),
    SIGN_UP_SUCCESS(2001 ,"Please check you email");

    AuthSituation(int code, String description) {
        this.code = code;
        this.description = description;
    }

    private int code;

    private String description;
}
