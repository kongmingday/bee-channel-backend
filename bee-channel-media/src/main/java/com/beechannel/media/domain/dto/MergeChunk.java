package com.beechannel.media.domain.dto;

import lombok.Data;

/**
 * @Description chunk merge params
 * @Author eotouch
 * @Date 2024/01/02 22:59
 * @Version 1.0
 */
@Data
public class MergeChunk {

    private String fileHash;
    private int chunkCount;
}
