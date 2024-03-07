package com.beechannel.order.constant;

import lombok.Getter;

/**
 * @Description pay process status
 * @Author eotouch
 * @Date 2024/01/16 21:17
 * @Version 1.0
 */
@Getter
public enum PayProcessStatus {

    UNPAID(100101, "TRADE_CLOSED", "no paid"),
    PAID(100102, "TRADE_SUCCESS", "paid");

    private final int id;
    private final String aliStatus;
    private final String description;

    PayProcessStatus(int id,String aliStatus, String description) {
        this.id = id;
        this.aliStatus = aliStatus;
        this.description = description;
    }
}
