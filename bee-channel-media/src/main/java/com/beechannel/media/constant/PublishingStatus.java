package com.beechannel.media.constant;

import lombok.Getter;

/**
 * @Description the publishing status of the playlist
 * @Author eotouch
 * @Date 2023/12/11 15:26
 * @Version 1.0
 */
@Getter
public enum PublishingStatus {

    PRIVATE(0, "private"),
    PUBLISHING(1, "publishing");

    private final Integer code;
    private final String description;

    PublishingStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
