package com.beechannel.order.service;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.order.domain.dto.PayRecordParam;
import com.beechannel.order.domain.po.Record;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author eotouch
* @description 针对表【record】的数据库操作Service
* @createDate 2024-01-16 21:33:45
*/
public interface RecordService extends IService<Record> {

    RestResponse qrcGenerate(PayRecordParam payRecordParam);
}
