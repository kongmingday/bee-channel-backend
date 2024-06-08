package com.beechannel.live.service.impl;

import cn.hutool.core.util.IdUtil;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.live.constant.CensorshipStatus;
import com.beechannel.live.constant.DefaultCredit;
import com.beechannel.live.domain.bo.CensorshipExtend;
import com.beechannel.live.domain.po.Live;
import com.beechannel.live.domain.po.SuperviseLicense;
import com.beechannel.live.mapper.LiveMapper;
import com.beechannel.live.mapper.SuperviseLicenseMapper;
import com.beechannel.live.service.CensorshipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * own censorship service
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/10 14:25
 */
@Service
public class OwnCensorshipImpl implements CensorshipService {

    @Resource
    private SuperviseLicenseMapper superviseLicenseMapper;

    @Resource
    private LiveMapper liveMapper;

    private final Long DEFAULT_ID = 0L;

    /**
     * simple handle to pass
     *
     * @param censorshipExtend censorship extend message
     */
    public void censorHandle(CensorshipExtend censorshipExtend) {
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();

        SuperviseLicense license = new SuperviseLicense();
        license.setUserId(currentUserId);
        license.setStatus(CensorshipStatus.APPROVAL.getCode());
        license.setCreateTime(LocalDateTime.now());
        license.setSuperviseTime(LocalDateTime.now());
        license.setUserId(DEFAULT_ID);
        superviseLicenseMapper.insert(license);

        Live live = new Live();
        live.setUserId(currentUserId);
        live.setLiveKey(IdUtil.getSnowflakeNextIdStr());
        live.setLiveSecret(IdUtil.fastUUID());
        live.setStatus(CensorshipStatus.APPROVAL.getCode());
        live.setCureentStatus(0);
        live.setCreditScore(DefaultCredit.DEFAULT_CREDIT.getScore());
        liveMapper.insert(live);
    }

}
