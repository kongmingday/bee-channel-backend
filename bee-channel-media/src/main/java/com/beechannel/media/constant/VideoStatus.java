package com.beechannel.media.constant;

import lombok.Getter;

/**
 * @Description video's status
 * @Author eotouch
 * @Date 2023/11/30 21:23
 * @Version 1.0
 */
@Getter
public enum VideoStatus {

    UNREVIEWED(0, "unreviewed"),
    UNAPPROVED(1, "unapprove"),
    APPROVE(2, "approve");

    private int id;
    private String message;

    VideoStatus(int id, String message) {
        this.id = id;
        this.message = message;
    }
}
