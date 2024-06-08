package com.beechannel.live.service;

import com.beechannel.live.domain.bo.CensorshipExtend;

/**
 * censorship interface
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/10 14:09
 */
public interface CensorshipService {

    void censorHandle(CensorshipExtend censorshipExtend);
}
