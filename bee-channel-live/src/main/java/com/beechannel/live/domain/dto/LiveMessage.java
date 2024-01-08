package com.beechannel.live.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description live message dto
 * @Author eotouch
 * @Date 2024/01/06 21:51
 * @Version 1.0
 */
@Data
public class LiveMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String roomId;

    private String profile;

    private String username;

    private String message;

    private boolean system;

}
