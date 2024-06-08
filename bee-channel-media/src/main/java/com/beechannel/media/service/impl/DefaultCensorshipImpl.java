package com.beechannel.media.service.impl;

import com.beechannel.media.constant.CensorshipStatus;
import com.beechannel.media.domain.bo.CensorshipExtend;
import com.beechannel.media.domain.po.Supervise;
import com.beechannel.media.service.CensorshipService;

/**
 * default censorship service
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/10 14:25
 */
public class DefaultCensorshipImpl implements CensorshipService {

    /**
     * simple handle to pass
     *
     * @param censorshipExtend censorship extend message
     */
    public void censorHandle(CensorshipExtend censorshipExtend) {
        Supervise supervise = (Supervise) censorshipExtend.getData();
        supervise.setStatus(CensorshipStatus.WAITING.getCode());
    }
}
