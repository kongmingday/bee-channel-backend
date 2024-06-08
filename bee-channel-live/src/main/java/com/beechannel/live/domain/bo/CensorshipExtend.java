package com.beechannel.live.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * censorship extend message
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/23 14:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CensorshipExtend<T> {

    private T data;

    public static <T> CensorshipExtend<T> of(T data){
        return new CensorshipExtend<>(data);
    }
}
