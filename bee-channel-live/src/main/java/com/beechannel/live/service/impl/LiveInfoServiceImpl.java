package com.beechannel.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.constant.InnerRpcStatus;
import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.live.domain.dto.LiveUserInfo;
import com.beechannel.live.domain.po.Live;
import com.beechannel.live.domain.po.LiveInfo;
import com.beechannel.live.feign.UserClient;
import com.beechannel.live.mapper.LiveInfoMapper;
import com.beechannel.live.mapper.LiveMapper;
import com.beechannel.live.service.LiveInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author eotouch
* @description 针对表【live_info】的数据库操作Service实现
* @createDate 2024-01-05 20:48:57
*/
@Service
public class LiveInfoServiceImpl extends ServiceImpl<LiveInfoMapper, LiveInfo>
    implements LiveInfoService{

    @Resource
    private LiveMapper liveMapper;

    @Resource
    private LiveInfoMapper liveInfoMapper;

    @Resource
    private UserClient userClient;

    /**
     * @description get live infomationn by live key id
     * @param liveKeyId
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2024-01-07 15:42
     */
    @Override
    public RestResponse getLiveInfo(String liveKeyId) {
        LambdaQueryWrapper<Live> liveQuery = new LambdaQueryWrapper<>();
        liveQuery.eq(Live::getLiveKey, liveKeyId);
        Live live = liveMapper.selectOne(liveQuery);
        if(live == null){
            return RestResponse.validFail("this live is not exist");
        }

        Long liveId = live.getId();
        LambdaQueryWrapper<LiveInfo> liveInfoQuery = new LambdaQueryWrapper<>();
        liveInfoQuery.eq(LiveInfo::getLiveId, liveId);
        LiveInfo liveInfo = liveInfoMapper.selectOne(liveInfoQuery);
        if(liveInfo == null){
            return RestResponse.validFail("this live is not exist");
        }

        Long currentUserId = SecurityUtil.getCurrentUserId();
        Long liveUserId = live.getUserId();
        RestResponse<FullUser> userInfo = userClient.getFullUserInfo(liveUserId, currentUserId);
        if(userInfo.getCode() == InnerRpcStatus.ERROR.getCode()){
            return RestResponse.validFail("get live user information has error");
        }

        LiveUserInfo liveUserInfo = new LiveUserInfo();
        BeanUtils.copyProperties(liveInfo, liveUserInfo);
        liveUserInfo.setAuthor(userInfo.getResult());
        return RestResponse.success(liveUserInfo);
    }
}




