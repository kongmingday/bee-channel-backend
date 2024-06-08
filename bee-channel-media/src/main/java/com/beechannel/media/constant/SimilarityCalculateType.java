package com.beechannel.media.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * similarity calculate type
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/07 22:43
 */
@Getter
@AllArgsConstructor
public enum SimilarityCalculateType {

    ACCUMULATION("accumulation"),
    IUF("iuf")
    ;

    private String code;
}
