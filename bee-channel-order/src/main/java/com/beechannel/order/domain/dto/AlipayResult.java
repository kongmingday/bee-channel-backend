package com.beechannel.order.domain.dto;

import lombok.Data;

/**
 * @Description alipay result
 * @Author eotouch
 * @Date 2024/02/09 14:41
 * @Version 1.0
 */
@Data
public class AlipayResult {

    private AlipayDetail alipay_trade_precreate_response;

    private String sign;

    @Data
    public class AlipayDetail {
        private String msg;
        private String code;
        private String out_trade_no;
        private String qr_code;
    }
}
