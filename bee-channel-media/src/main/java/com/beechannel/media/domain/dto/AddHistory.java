package com.beechannel.media.domain.dto;

import lombok.Data;

/**
 * @Description the history of the insert action
 * @Author eotouch
 * @Date 2024/02/13 17:54
 * @Version 1.0
 */
@Data
public class AddHistory {

    private Long videoId;
    private Long duration;
    private String pausePoint;
}
