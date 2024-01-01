package com.beechannel.base.constant;

import lombok.Getter;

/**
 * @Description inner rpc process status
 * @Author eotouch
 * @Date 2023/12/21 10:20
 * @Version 1.0
 */
@Getter
public enum InnerRpcStatus {

    ERROR(-1, "rpc error"),
    SUCCESS(200, "rpc success");

    private final Integer code;
    private final String description;

    InnerRpcStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
