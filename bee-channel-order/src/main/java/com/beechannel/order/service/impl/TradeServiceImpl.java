package com.beechannel.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.order.domain.po.Trade;
import com.beechannel.order.service.TradeService;
import com.beechannel.order.mapper.TradeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
* @author eotouch
* @description 针对表【trade】的数据库操作Service实现
* @createDate 2024-02-09 16:07:20
*/
@Service
public class TradeServiceImpl extends ServiceImpl<TradeMapper, Trade>
    implements TradeService {

    @Resource
    private TradeMapper tradeMapper;

    @Override
    public RestResponse getLiveIncomeAmount() {

        Long userIdNotNull = SecurityUtil.getCurrentUserIdNotNull();

        LambdaQueryWrapper<Trade> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Trade::getUserId, userIdNotNull);

        List<Trade> trades = tradeMapper.selectList(queryWrapper);

        AtomicReference<BigDecimal> income = new AtomicReference<>(BigDecimal.valueOf(0));
        trades.forEach(item -> {
            BigDecimal accumulation = income.get().add(item.getTotalPrice());
            income.set(accumulation);
        });

        return RestResponse.success(income.get());
    }
}




