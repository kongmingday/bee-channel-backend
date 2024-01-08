package com.beechannel.media.domain.dto;

import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.media.domain.po.Video;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description video's information with author's message
 * @Author eotouch
 * @Date 2023/11/29 19:54
 * @Version 1.0
 */
@Data
public class SingleVideo extends Video implements Serializable {

    private static final long serialVersionUID = 1L;

    private FullUser author;

    private Long commentCount;

    private Integer likeCount;

    private Integer unlikeCount;

    private Integer favoriteType;
}
