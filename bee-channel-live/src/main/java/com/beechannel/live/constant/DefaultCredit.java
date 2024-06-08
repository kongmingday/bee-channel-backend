package com.beechannel.live.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * default credit score
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/25 0:09
 */
@AllArgsConstructor
@Getter
public enum DefaultCredit {

    DEFAULT_CREDIT(10);

    private final Integer score;
}
