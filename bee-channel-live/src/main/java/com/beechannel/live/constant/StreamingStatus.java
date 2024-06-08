package com.beechannel.live.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * censorship status
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/24 23:42
 */
@Getter
@AllArgsConstructor
public enum StreamingStatus {

    APPROVED(0, "approved"),
    UNAPPROVED(1, "unapproved");
    ;

    private final Integer code;

    private final String name;
}
