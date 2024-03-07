package com.beechannel.media.domain.dto;

import com.beechannel.media.domain.po.History;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description the history with the video information of the user's action
 * @Author eotouch
 * @Date 2024/02/14 15:47
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HistoryVideo extends History {

    private SingleVideo video;
}
