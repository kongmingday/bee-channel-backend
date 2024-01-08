package com.beechannel.live.domain.bo;

import lombok.Data;

import javax.websocket.Session;
import java.io.Serializable;

/**
 * @Description websocket storage
 * @Author eotouch
 * @Date 2024/01/06 16:44
 * @Version 1.0
 */
@Data
public class WebSocketStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private boolean isAnonymous;

    private Session session;

    private String roomId;

}
