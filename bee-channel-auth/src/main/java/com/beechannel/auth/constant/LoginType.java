package com.beechannel.auth.constant;

/**
 * @Description TODO
 * @Author eotouch
 * @Date 2023/11/17 23:36
 * @Version 1.0
 */
public enum LoginType {

    WECHAT("WeChat"),
    PASSWORD("Password"),
    EMAIL("Email");


    private String name;


    LoginType(String name) {
        this.name = name;
    }
}
