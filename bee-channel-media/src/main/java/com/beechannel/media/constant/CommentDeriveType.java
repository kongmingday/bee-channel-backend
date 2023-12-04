package com.beechannel.media.constant;

import lombok.Getter;

/**
 * @Description derive type
 * @Author eotouch
 * @Date 2023/12/03 22:35
 * @Version 1.0
 */
@Getter
public enum CommentDeriveType {
    VIDEO(0, "video"),
    DYNAMIC(1, "dynamic");

    private final int code;
    private final String name;

    CommentDeriveType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
