package com.beechannel.order.constant;

import lombok.Getter;

/**
 * @Description pay process message detail
 * @Author eotouch
 * @Date 2024/01/12 9:34
 * @Version 1.0
 */
@Getter
public enum PayProcessDetail {
    CREATE_REQUEST_ERROR(10001, "create pay request has error"),
    NOTIFY_VERIFY_ERROR(10002, "notified verify has error"),
    NOTIFY_PROCESS_ERROR(10003, "notified process has error");

    private final int id;
    private final String description;

    PayProcessDetail(int id, String description) {
        this.id = id;
        this.description = description;
    }
}
