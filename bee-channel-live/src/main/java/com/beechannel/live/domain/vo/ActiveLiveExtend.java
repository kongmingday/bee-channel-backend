package com.beechannel.live.domain.vo;

import com.beechannel.live.domain.dto.ActiveLiveInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * active live info extend
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/11 10:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ActiveLiveExtend extends ActiveLiveInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String profile;
}
