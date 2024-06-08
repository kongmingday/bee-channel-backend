package com.beechannel.media.domain.bo;

import lombok.Data;

/**
 * censorship extend message
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/23 14:13
 */
@Data
public class CensorshipExtend<T> {

    private T data;
}
