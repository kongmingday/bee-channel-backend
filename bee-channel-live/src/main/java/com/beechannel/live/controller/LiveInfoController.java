package com.beechannel.live.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.live.domain.po.LiveInfo;
import com.beechannel.live.service.LiveInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description live info service
 * @Author eotouch
 * @Date 2024/01/05 21:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/info")
public class LiveInfoController {

    @Resource
    private LiveInfoService liveInfoService;

    @PostMapping
    public RestResponse updateLiveInfo(@RequestBody LiveInfo liveInfo){
        boolean updateResult = liveInfoService.saveOrUpdate(liveInfo);
        return RestResponse.success(updateResult);
    }

    @GetMapping("/personal/{liveId}")
    public RestResponse getPersonalLiveInfoConfig(@PathVariable Long liveId){
        LambdaQueryWrapper<LiveInfo> liveInfoQuery = new LambdaQueryWrapper<>();
        liveInfoQuery.eq(LiveInfo::getLiveId, liveId);
        LiveInfo liveInfo = liveInfoService.getOne(liveInfoQuery);
        return RestResponse.success(liveInfo);
    }

    @GetMapping("/{liveKeyId}")
    public RestResponse getLiveInfo(@PathVariable String liveKeyId){
        return liveInfoService.getLiveInfo(liveKeyId);
    }
}
