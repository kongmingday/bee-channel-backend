package com.beechannel.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.base.constant.InnerRpcStatus;
import com.beechannel.base.constant.RabbitMqValue;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.order.config.AlipayConfig;
import com.beechannel.order.constant.PayProcessDetail;
import com.beechannel.order.constant.PayProcessStatus;
import com.beechannel.order.domain.dto.LiveMessage;
import com.beechannel.order.domain.dto.PayRecordParam;
import com.beechannel.order.domain.po.Trade;
import com.beechannel.order.feign.UserClient;
import com.beechannel.order.mapper.TradeMapper;
import com.beechannel.order.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description pay service implement
 * @Author eotouch
 * @Date 2024/01/20 21:04
 * @Version 1.0
 */
@Service
@Slf4j
@RefreshScope
public class PayServiceImpl implements PayService {

    @Value("${pay.alipay.ALIPAY_PUBLIC_KEY}")
    private String ALIPAY_PUBLIC_KEY;

    @Resource
    private TradeMapper tradeMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private UserClient userClient;

    /**
     * @description notify the pay result
     * @param request request
     * @return void
     * @author eotouch
     * @date 2024-01-25 21:36
     */
    @Override
    public void notifyRequest(HttpServletResponse response, HttpServletRequest request) {

        // get the alipay message from request
        Map<String, String> params = getAlipayRequestMessage(request);
        // check whether the payment was made by system
        boolean verifyResult = false;
        try{
            verifyResult = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
        }catch (Exception e) {
            BeeChannelException.cast(PayProcessDetail.NOTIFY_VERIFY_ERROR.getDescription());
        }
        if(!verifyResult){
            return;
        }

        // search the trade in the db
        String outTradeNo = params.get("out_trade_no");
        LambdaQueryWrapper<Trade> tradeQueryWrapper = new LambdaQueryWrapper<>();
        tradeQueryWrapper.eq(Trade::getId, outTradeNo);
        Long count = tradeMapper.selectCount(tradeQueryWrapper);

        // insert new data when the trade doesn't exist in the db
        if(count <= 0){
            String tradeStatus = params.get("trade_status");
            double totalAmount = Double.parseDouble(params.get("total_amount"));
            String businessJson = params.get("body");
            PayRecordParam payRecordParam = JSON.parseObject(businessJson, PayRecordParam.class);
            PayProcessStatus payProcessStatus = Arrays.stream(PayProcessStatus.values())
                    .filter(item -> item.getAliStatus().equals(tradeStatus))
                    .findFirst().get();

            Long userId = payRecordParam.getUserId();
            BigDecimal totalPrice = BigDecimal.valueOf(totalAmount);
            RestResponse<User> userInfo = userClient.getUserInfo(userId);
            User result = userInfo.getResult();
            if(InnerRpcStatus.ERROR.getCode() == userInfo.getCode()){
                return;
            }

            LiveMessage liveMessage = new LiveMessage();
            liveMessage.setAmount(totalPrice);
            liveMessage.setMessage(payRecordParam.getMessage());
            liveMessage.setProfile(result.getProfile());
            liveMessage.setRoomId(payRecordParam.getDeriveId());
            liveMessage.setUsername(result.getUsername());
            liveMessage.setUserId(userId.toString());

            rabbitTemplate.convertAndSend(
                    RabbitMqValue.LIVE_MESSAGE_EXCHANGE,
                    RabbitMqValue.LIVE_MESSAGE_KEY,
                    liveMessage
            );

            Trade trade = new Trade();
            trade.setCreateTime(LocalDateTime.now());
            trade.setId(Long.valueOf(outTradeNo));
            trade.setStatus(payProcessStatus.getId());
            trade.setDetails(businessJson);
            trade.setDeriveId(payRecordParam.getDeriveId());
            trade.setUserId(userId);
            trade.setTotalPrice(totalPrice);
            trade.setDescription(userId + " pay " + payRecordParam.getTotalPrice() + " yuan to " + payRecordParam.getDeriveId());
            tradeMapper.insert(trade);


        }

        // notify alipay that the information processing is successful
        try (PrintWriter writer = response.getWriter()) {
            writer.write("success");
            writer.flush();
        }catch (Exception e){
            BeeChannelException.cast(PayProcessDetail.NOTIFY_PROCESS_ERROR.getDescription());
        }
    }

    /**
     * @description build the map from request
     * @param request http request
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author eotouch
     * @date 2024-02-09 17:09
     */
    @NotNull
    private static Map<String, String> getAlipayRequestMessage(HttpServletRequest request) {
        Map<String,String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        requestParams.forEach((key, value) -> {
            String valueStr = "";
            for (int i = 0; i < value.length; i++) {
                valueStr = (i == value.length - 1) ? valueStr + value[i]
                        : valueStr + value[i] + ",";
            }
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(key, valueStr);
        });
        return params;
    }

}
