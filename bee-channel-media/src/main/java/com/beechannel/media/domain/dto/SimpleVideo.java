package com.beechannel.media.domain.dto;

import com.beechannel.base.domain.po.User;
import com.beechannel.media.domain.po.Video;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author eotouch
 * @Date 2024/02/13 11:20
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SimpleVideo extends Video implements Serializable {

    private static final long serialVersionUID = 1L;

    private User author;
}
