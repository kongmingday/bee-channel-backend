package com.beechannel.media.constant;

import lombok.Getter;

/**
 * @Description favorite type
 * @Author eotouch
 * @Date 2023/12/08 21:06
 * @Version 1.0
 */
@Getter
public enum FavoriteType {
    UNLIKE(0, "unlike"),
    LIKE(1, "like");

    private final Integer code;
    private final String type;

    FavoriteType(Integer code, String type) {
        this.code = code;
        this.type = type;
    }
}
