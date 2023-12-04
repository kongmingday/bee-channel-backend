package com.beechannel.base.constant;

import lombok.Getter;

/**
 * @Description user status
 * @Author eotouch
 * @Date 2023/11/16 10:43
 * @Version 1.0
 */
@Getter
public enum UserStatus {

    DISABLE("未启用", 0),
    ENABLE("启用", 1);

    private String name;

    private Integer code;

    UserStatus(String name, Integer code) {
        this.name = name;
        this.code = code;
    }
}
