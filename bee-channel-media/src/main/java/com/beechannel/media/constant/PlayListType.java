package com.beechannel.media.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * play list type enum
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/09 16:56
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PlayListType {

    NORMAL("Normal"),
    WATCH_LATER("Watch Later")
    ;

    private String name;
}
