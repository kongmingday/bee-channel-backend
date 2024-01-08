package com.beechannel.live.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description SRS request params
 * @Author eotouch
 * @Date 2024/01/04 23:17
 * @Version 1.0
 */
@Data
public class SRSRequestParams implements Serializable {

    private static final long serialVersionUID = 1L;

    private String stream;
    private String param;
}
