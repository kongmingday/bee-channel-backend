package com.beechannel.media.domain.dto;

import com.beechannel.media.domain.po.Supervise;
import com.beechannel.media.domain.po.Video;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description audit video item
 * @Author eotouch
 * @Date 2023/12/29 21:57
 * @Version 1.0
 */
@Data
public class AuditVideoItem extends Video implements Serializable {

    private static final long serialVersionUID = 1L;

    private Supervise supervise;

}
