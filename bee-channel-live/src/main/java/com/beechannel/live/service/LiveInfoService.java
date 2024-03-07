package com.beechannel.live.service;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.live.domain.po.LiveInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author eotouch
* @description 针对表【live_info】的数据库操作Service
* @createDate 2024-01-05 20:48:57
*/
public interface LiveInfoService extends IService<LiveInfo> {

    RestResponse getLiveInfo(String liveKeyId);
}
