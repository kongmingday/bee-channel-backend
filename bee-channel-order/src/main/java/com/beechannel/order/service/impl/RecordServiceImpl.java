package com.beechannel.order.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.QRCodeUtil;
import com.beechannel.base.util.RedisCacheStore;
import com.beechannel.order.config.AlipayConfig;
import com.beechannel.order.constant.PayProcessDetail;
import com.beechannel.order.domain.dto.AlipayResult;
import com.beechannel.order.domain.dto.PayRecordParam;
import com.beechannel.order.domain.po.Record;
import com.beechannel.order.mapper.RecordMapper;
import com.beechannel.order.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author eotouch
* @description 针对表【record】的数据库操作Service实现
* @createDate 2024-01-16 21:33:45
*/
@Service
@RefreshScope
@Slf4j
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record>
    implements RecordService{

    @Value("${pay.alipay.APP_ID}")
    private String APP_ID;

    @Value("${pay.alipay.APP_PRIVATE_KEY}")
    private String APP_PRIVATE_KEY;

    @Value("${pay.alipay.ALIPAY_PUBLIC_KEY}")
    private String ALIPAY_PUBLIC_KEY;

    @Value("${pay.return-call-url}")
    private String RETURN_CALL_URL;

    @Resource
    private RedisCacheStore redisCacheStore;

    /**
     * @description set params to redis and return qrcCode by alipay
     * @param payRecordParam record params
     * @return com.beechannel.base.domain.vo.RestResponse qrc code result
     * @author eotouch
     * @date 2024-01-21 23:53
     */
    @Override
    public RestResponse qrcGenerate(PayRecordParam payRecordParam) {

        // set params to redis
        String qrCode = null;
        String tradeId = IdUtil.getSnowflakeNextIdStr();

        // set alipay client params
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.URL,
                APP_ID, APP_PRIVATE_KEY,
                AlipayConfig.FORMAT,
                AlipayConfig.CHARSET,
                ALIPAY_PUBLIC_KEY,
                AlipayConfig.SIGNTYPE
        );

        // set alipay trade request params
        AlipayTradePrecreateRequest payRequest = buildAlipayPayRequest(tradeId, payRecordParam);
        try {

            // send request to alipay
            AlipayTradePrecreateResponse payResponse = alipayClient.execute(payRequest, "POST");
            if(!payResponse.isSuccess()){
                BeeChannelException.cast(payResponse.getMsg());
            }

            // get the target url and generate qr code
            String result = payResponse.getBody();
            AlipayResult alipayResult = JSON.parseObject(result, AlipayResult.class);
            String targetCode = alipayResult.getAlipay_trade_precreate_response().getQr_code();
            qrCode = new QRCodeUtil().createQRCode(targetCode, 200, 200);
        }catch (Exception e){
            e.printStackTrace();
            BeeChannelException.cast(PayProcessDetail.CREATE_REQUEST_ERROR.getDescription());
        }

        return RestResponse.success(qrCode);
    }

    /**
     * @description build the alipay request
     * @param tradeId the trade's id
     * @param payRecordParam the pay param
     * @return com.alipay.api.request.AlipayTradePrecreateRequest
     * @author eotouch
     * @date 2024-02-09 15:35
     */
    @NotNull
    private AlipayTradePrecreateRequest buildAlipayPayRequest(String tradeId, PayRecordParam payRecordParam) {
        AlipayTradePrecreateRequest payRequest = new AlipayTradePrecreateRequest();
        payRequest.setNotifyUrl(RETURN_CALL_URL);
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        String price = String.valueOf(payRecordParam.getTotalPrice());
        model.setOutTradeNo(tradeId);
        model.setTotalAmount(price);
        model.setSubject("Live tipping");
        model.setBody(JSON.toJSONString(payRecordParam));
        payRequest.setBizModel(model);
        return payRequest;
    }
}




