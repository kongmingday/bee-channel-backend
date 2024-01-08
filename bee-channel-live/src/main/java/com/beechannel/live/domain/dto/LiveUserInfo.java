package com.beechannel.live.domain.dto;

import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.live.domain.po.LiveInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description the live information and user information
 * @Author eotouch
 * @Date 2024/01/07 15:59
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiveUserInfo extends LiveInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private FullUser author;
}
