package com.beechannel.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.constant.AuditStatus;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.live.constant.LiveStatus;
import com.beechannel.live.domain.dto.LicenseResult;
import com.beechannel.live.domain.dto.SRSRequestParams;
import com.beechannel.live.domain.po.Live;
import com.beechannel.live.domain.po.LiveInfo;
import com.beechannel.live.domain.po.SuperviseLicense;
import com.beechannel.live.mapper.LiveInfoMapper;
import com.beechannel.live.mapper.SuperviseLicenseMapper;
import com.beechannel.live.service.LiveInfoService;
import com.beechannel.live.service.LiveService;
import com.beechannel.live.mapper.LiveMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
            return RestResponse.success("no apply");
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
            return 1;
        }

        // check the key and the secret
        LambdaQueryWrapper<Live> liveQuery = new LambdaQueryWrapper<>();
        String secret = param.substring(1).split("=")[1];
        liveQuery.eq(Live::getLiveKey, stream);
        liveQuery.eq(Live::getLiveSecret, secret);
        Live live = liveMapper.selectOne(liveQuery);
        if (live == null) {
            return 1;
        }

        LambdaQueryWrapper<LiveInfo> liveInfoQuery = new LambdaQueryWrapper<>();
        liveInfoQuery.eq(LiveInfo::getLiveId, live.getId());
        LiveInfo liveInfo = liveInfoMapper.selectOne(liveInfoQuery);
        if(liveInfo == null){
            return 1;
        }

        // set live current status
        live.setCureentStatus(LiveStatus.ACTIVE.getCode());
        boolean uploadResult = liveMapper.updateById(live) > 0;
        if(!uploadResult){
            return 1;
        }
        return 0;
    }

    /**
     * @description upload live status on stop publish
     * @param srsRequestParams the srs request param
     * @return int
     * @author eotouch
     * @date 2024-01-05 16:39
     */
    @Override
    public int stopProcess(SRSRequestParams srsRequestParams) {
        String stream = srsRequestParams.getStream();
        String param = srsRequestParams.getParam();
        if (!StringUtils.hasText(stream) || !StringUtils.hasText(param)) {
            return 0;
        }

        LambdaQueryWrapper<Live> liveQuery = new LambdaQueryWrapper<>();
        String secret = param.substring(1).split("=")[1];
        liveQuery.eq(Live::getLiveKey, stream);
        liveQuery.eq(Live::getLiveSecret, secret);
        Live live = liveMapper.selectOne(liveQuery);
        if (live == null) {
            return 0;
        }

        // set live current status
        live.setCureentStatus(LiveStatus.INACTIVE.getCode());
        liveMapper.updateById(live);
        return 0;
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
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();

        SuperviseLicense license = new SuperviseLicense();
        license.setUserId(currentUserId);
        license.setStatus(0);
        license.setCreateTime(LocalDateTime.now());
        superviseLicenseMapper.insert(license);

        return RestResponse.success();
    }

}




