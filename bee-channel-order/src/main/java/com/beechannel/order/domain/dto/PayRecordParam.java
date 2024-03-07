package com.beechannel.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description pay record params
 * @Author eotouch
 * @Date 2024/01/16 21:36
 * @Version 1.0
 */
@Data
public class PayRecordParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal totalPrice;
    private String payType;
    private String deriveId;
    private String message;
    private Long userId;
}
