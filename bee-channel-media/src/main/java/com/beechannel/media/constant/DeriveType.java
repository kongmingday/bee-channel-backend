package com.beechannel.media.constant;

import lombok.Getter;

/**
 * @Description derive type
 * @Author eotouch
 * @Date 2023/12/03 22:35
 * @Version 1.0
 */
@Getter
public enum DeriveType {
    VIDEO(0, "video"),
    DYNAMIC(1, "dynamic"),
    COMMENT(2, "comment");

    private final Integer code;
    private final String name;

    DeriveType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
