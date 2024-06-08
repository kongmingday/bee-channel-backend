package com.beechannel.media.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * censorship status
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/23 15:54
 */
@Getter
@AllArgsConstructor
public enum CensorshipStatus {

    WAITING(0, "waiting"),
    UNAPPROVED(1, "unapproved"),
    APPROVAL(2, "approved");


    private final Integer code;

    private final String name;
}
