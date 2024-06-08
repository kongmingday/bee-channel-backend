package com.beechannel.base.domain.dto;

import com.beechannel.base.domain.po.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description user' message with concern
 * @Author eotouch
 * @Date 2023/12/03 14:04
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FullUser extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean hasConcern;

    private Integer subscribeCount;
}
