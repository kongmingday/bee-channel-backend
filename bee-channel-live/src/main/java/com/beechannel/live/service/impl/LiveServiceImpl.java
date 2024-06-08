package com.beechannel.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.constant.AuditStatus;
import com.beechannel.base.constant.InnerRpcStatus;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.PageResult;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.RedisCacheStore;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.live.constant.RedisStoreSpaceKey;
import com.beechannel.live.constant.StreamingStatus;
import com.beechannel.live.domain.bo.CensorshipExtend;
import com.beechannel.live.domain.dto.ActiveLiveInfo;
import com.beechannel.live.domain.dto.LicenseResult;
import com.beechannel.live.domain.dto.SRSRequestParams;
import com.beechannel.live.domain.po.Live;
import com.beechannel.live.domain.po.SuperviseLicense;
import com.beechannel.live.domain.vo.ActiveLiveExtend;
import com.beechannel.live.feign.UserClient;
import com.beechannel.live.mapper.LiveInfoMapper;
import com.beechannel.live.mapper.LiveMapper;
import com.beechannel.live.mapper.SuperviseLicenseMapper;
import com.beechannel.live.service.CensorshipService;
import com.beechannel.live.service.LiveService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author eotouch
 * @description 针对表【live】的数据库操作Service实现
 * @createDate 2024-01-05 11:15:36
 */
@Service
public class LiveServiceImpl extends ServiceImpl<LiveMapper, Live>
        implements LiveService {

    @Resource
    private SuperviseLicenseMapper superviseLicenseMapper;

    @Resource
    private LiveInfoMapper liveInfoMapper;

    @Resource
    private LiveMapper liveMapper;

    @Resource
    private UserClient userClient;

    @Resource
    private CensorshipService censorshipService;

    @Resource
    private RedisCacheStore redisCacheStore;

    /**
     * get the active live
     *
     * @param pageParams page params
     * @return
     */
    @Override
    public RestResponse getActiveLivePage(PageParams pageParams) {
        List<String> fromList = redisCacheStore.getFromList(
                RedisStoreSpaceKey.LIVE_ROOM_ACTIVE_SIGNAL,
                pageParams.getPageNo(), pageParams.getPageSize()
        );

        List<ActiveLiveExtend> collect = fromList.stream().map(item -> JSON.parseObject(item, ActiveLiveExtend.class))
                .collect(Collectors.toList());
        PageResult pageResult = new PageResult<>();
        if (collect.isEmpty()) {
            pageResult.setTotal(0);
            pageResult.setData(Collections.emptyList());
            return RestResponse.success(pageResult);
        }

        pageResult.setTotal(collect.size());
        pageResult.setData(collect);
        return RestResponse.success(pageResult);

//        IPage<ActiveLiveInfo> pageInfo = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
//        liveInfoMapper.getActiveLivePage(pageInfo);
//
//        List<ActiveLiveInfo> result = pageInfo.getRecords();
//        List<Long> userList = result.stream().map(ActiveLiveInfo::getUserId).collect(Collectors.toList());
//
//        if(userList.isEmpty()){
//            PageResult pageResult = new PageResult<>();
//            pageResult.setTotal(0);
//            pageResult.setData(Collections.emptyList());
//            return RestResponse.success(pageResult);
//        }
//
//        RestResponse<List<User>> responseResult = userClient.getUserInfoByIdList(userList);
//
//        if (responseResult.getCode() == InnerRpcStatus.ERROR.getCode()) {
//            BeeChannelException.cast("search live has error");
//        }
//
//        Map<Long, List<User>> userMap = responseResult.getResult().stream()
//                .collect(Collectors.groupingBy(User::getId));
//
//        List<ActiveLiveExtend> finalResult = result.stream().map(item -> {
//            ActiveLiveExtend activeLiveExtend = new ActiveLiveExtend();
//            BeanUtils.copyProperties(item, activeLiveExtend);
//
//            activeLiveExtend.setProfile(userMap.get(item.getUserId()).get(0).getProfile());
//            activeLiveExtend.setUsername(userMap.get(item.getUserId()).get(0).getUsername());
//
//            return activeLiveExtend;
//        }).collect(Collectors.toList());
//
//
//        long total = pageInfo.getTotal();
//        PageResult pageResult = new PageResult<>();
//        pageResult.setTotal((int) total);
//        pageResult.setData(finalResult);
//        return RestResponse.success(pageResult);
    }

    /**
     * @return com.beechannel.base.domain.vo.RestResponse
     * @description get personal live license by current user
     * @author eotouch
     * @date 2024-01-05 11:51
     */
    @Override
    public RestResponse getPersonalLicense() {

        // get application by current user
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();
        LambdaQueryWrapper<SuperviseLicense> licenseQuery = new LambdaQueryWrapper<>();
        licenseQuery.eq(SuperviseLicense::getUserId, currentUserId);
        SuperviseLicense license = superviseLicenseMapper.selectOne(licenseQuery);
        if (license == null) {
            return RestResponse.validFail("no apply");
        }

        // if the application is approved, get license information
        LicenseResult licenseResult = new LicenseResult();
        Integer status = license.getStatus();
        if (AuditStatus.APPROVE.getId() == status) {
            LambdaQueryWrapper<Live> liveQuery = new LambdaQueryWrapper<>();
            liveQuery.eq(Live::getUserId, currentUserId);
            Live live = liveMapper.selectOne(liveQuery);
            licenseResult.setLive(live);
        }
        licenseResult.setSuperviseLicense(license);

        return RestResponse.success(licenseResult);
    }

    /**
     * @param srsRequestParams the srs request param
     * @return int the return rule of the srs service, 0 - Accepted 1 - Unaccepted
     * @description check authorization by the srs request param
     * @author eotouch
     * @date 2024-01-05 15:34
     */
    @Override
    public int liveInitCheck(SRSRequestParams srsRequestParams) {
        String stream = srsRequestParams.getStream();
        String param = srsRequestParams.getParam();
        if (!StringUtils.hasText(stream) || !StringUtils.hasText(param)) {
            return StreamingStatus.UNAPPROVED.getCode();
        }

        // check the key and the secret
        String secret = param.substring(1).split("=")[1];
//        LambdaQueryWrapper<Live> liveQuery = new LambdaQueryWrapper<>();
//        liveQuery.eq(Live::getLiveKey, stream);
//        liveQuery.eq(Live::getLiveSecret, secret);
//        Live live = liveMapper.selectOne(liveQuery);
        String jsonString = null;
        try {
            jsonString = buildActiveLiveJson(stream, secret);
        } catch (Exception e) {
            return StreamingStatus.UNAPPROVED.getCode();
        }

//        if (live == null) {
//            return StreamingStatus.UNAPPROVED.getCode();
//        }
//
//        LambdaQueryWrapper<LiveInfo> liveInfoQuery = new LambdaQueryWrapper<>();
//        liveInfoQuery.eq(LiveInfo::getLiveId, live.getId());
//        LiveInfo liveInfo = liveInfoMapper.selectOne(liveInfoQuery);
//        if(liveInfo == null){
//            return StreamingStatus.UNAPPROVED.getCode();
//        }
//
//        // set live current status
//        live.setCureentStatus(LiveStatus.ACTIVE.getCode());
//        boolean uploadResult = liveMapper.updateById(live) > 0;


        redisCacheStore.addToList(RedisStoreSpaceKey.LIVE_ROOM_ACTIVE_SIGNAL, jsonString);
        return StreamingStatus.APPROVED.getCode();
    }

    /**
     * @param srsRequestParams the srs request param
     * @return int
     * @description upload live status on stop publish
     * @author eotouch
     * @date 2024-01-05 16:39
     */
    @Override
    public int stopProcess(SRSRequestParams srsRequestParams) {
        String stream = srsRequestParams.getStream();
        String param = srsRequestParams.getParam();
        if (!StringUtils.hasText(stream) || !StringUtils.hasText(param)) {
            return StreamingStatus.APPROVED.getCode();
        }

        String secret = param.substring(1).split("=")[1];
        String json = buildActiveLiveJson(stream, secret);
        redisCacheStore.removeInList(RedisStoreSpaceKey.LIVE_ROOM_ACTIVE_SIGNAL, json);

//        LambdaQueryWrapper<Live> liveQuery = new LambdaQueryWrapper<>();
//        liveQuery.eq(Live::getLiveKey, stream);
//        liveQuery.eq(Live::getLiveSecret, secret);
//        Live live = liveMapper.selectOne(liveQuery);
//        if (live == null) {
//            return StreamingStatus.APPROVED.getCode();
//        }
//
//        // set live current status
//
//        live.setCureentStatus(LiveStatus.INACTIVE.getCode());
//        liveMapper.updateById(live);
        return StreamingStatus.APPROVED.getCode();
    }

    /**
     * @return com.beechannel.base.domain.vo.RestResponse
     * @description delete license by current user
     * @author eotouch
     * @date 2024-01-05 15:41
     */
    @Override
    @Transactional
    public RestResponse deleteLicense() {
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();
        LambdaQueryWrapper<Live> liveQuery = new LambdaQueryWrapper<>();
        liveQuery.eq(Live::getUserId, currentUserId);
        liveMapper.delete(liveQuery);

        LambdaQueryWrapper<SuperviseLicense> licenseQuery = new LambdaQueryWrapper<>();
        licenseQuery.eq(SuperviseLicense::getUserId, currentUserId);
        superviseLicenseMapper.delete(licenseQuery);
        return RestResponse.success();
    }

    /**
     * @return com.beechannel.base.domain.vo.RestResponse
     * @description add license by current user
     * @author eotouch
     * @date 2024-01-05 15:41
     */
    @Override
    @Transactional
    public RestResponse addLicense() {
        censorshipService.censorHandle(CensorshipExtend.of(null));
        return RestResponse.success();
    }

    /**
     * build the active live information json
     *
     * @param stream
     * @param secret
     * @return String
     * @author eotouch
     * @date 2024-04-25 1:37
     */
    private String buildActiveLiveJson(String stream, String secret) {
        ActiveLiveInfo liveInformation = liveInfoMapper.getLiveInformationExtend(stream, secret);
        if (Objects.isNull(liveInformation)) {
            BeeChannelException.cast("no data");
        }
        ActiveLiveExtend activeLiveExtend = new ActiveLiveExtend();

        RestResponse<User> responseResult = userClient.getUserInfo(liveInformation.getUserId());
        if (responseResult.getCode() == InnerRpcStatus.ERROR.getCode()) {
            BeeChannelException.cast("no data");
        }

        BeanUtils.copyProperties(liveInformation, activeLiveExtend);
        User targetUser = responseResult.getResult();
        activeLiveExtend.setUsername(targetUser.getUsername());
        activeLiveExtend.setProfile(targetUser.getProfile());

        return JSON.toJSONString(activeLiveExtend);
    }

}




