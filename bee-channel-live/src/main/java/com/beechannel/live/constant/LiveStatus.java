package com.beechannel.live.constant;

import lombok.Getter;

/**
 * @Description live current status
 * @Author eotouch
 * @Date 2024/01/05 16:30
 * @Version 1.0
 */
@Getter
public enum LiveStatus {

    INACTIVE(0, "inactive"),
    ACTIVE(1, "active");

    private final int code;
    private final String description;

    LiveStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
