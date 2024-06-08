package com.beechannel.live.service.impl;


import com.beechannel.base.util.SecurityUtil;
import com.beechannel.live.constant.CensorshipStatus;
import com.beechannel.live.domain.bo.CensorshipExtend;
import com.beechannel.live.domain.po.SuperviseLicense;
import com.beechannel.live.mapper.SuperviseLicenseMapper;
import com.beechannel.live.service.CensorshipService;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * default censorship service
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/10 14:25
 */
public class DefaultCensorshipImpl implements CensorshipService {

    @Resource
    private SuperviseLicenseMapper superviseLicenseMapper;

    /**
     * simple handle to pass
     *
     * @param censorshipExtend censorship extend message
     */
    public void censorHandle(CensorshipExtend censorshipExtend) {
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();

        SuperviseLicense license = new SuperviseLicense();
        license.setUserId(currentUserId);
        license.setStatus(CensorshipStatus.WAITING.getCode());
        license.setCreateTime(LocalDateTime.now());
        superviseLicenseMapper.insert(license);
    }
}
