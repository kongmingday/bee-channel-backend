package com.beechannel.order.controller;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.order.domain.dto.PayRecordParam;
import com.beechannel.order.service.PayService;
import com.beechannel.order.service.RecordService;
import com.beechannel.order.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description order service
 * @Author eotouch
 * @Date 2024/01/09 21:12
 * @Version 1.0
 */
@Slf4j
@Controller
public class OrderController {

    @Resource
    private RecordService recordService;

    @Resource
    private PayService payService;

    @Resource
    private TradeService tradeService;

    @PostMapping("/qrc/generate")
    @ResponseBody
    public RestResponse qrcCodeGenerate(@RequestBody PayRecordParam payRecordParam) {
        return recordService.qrcGenerate(payRecordParam);
    }

    @PostMapping("/notify")
    public void notifyRequest(HttpServletResponse response, HttpServletRequest request){
        payService.notifyRequest(response, request);
    }

    @GetMapping("/amount")
    @ResponseBody
    public RestResponse getLiveIncomeAmount(){
        return tradeService.getLiveIncomeAmount();
    }
}
