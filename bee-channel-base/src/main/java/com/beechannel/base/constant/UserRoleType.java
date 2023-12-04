package com.beechannel.base.constant;

import lombok.Getter;

/**
 * @Description TODO
 * @Author eotouch
 * @Date 2023/11/16 21:13
 * @Version 1.0
 */
@Getter
public enum UserRoleType {

    SUPERVISOR(1, "A10001", "supervisor"),
    COMMON_USER(2, "A10002", "common");

    private Integer id;
    private String code;
    private String name;

    UserRoleType(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
