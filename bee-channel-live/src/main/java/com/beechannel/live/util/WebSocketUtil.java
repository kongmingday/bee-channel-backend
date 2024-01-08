package com.beechannel.live.util;

import com.alibaba.fastjson.JSON;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.live.endpoint.LivePoint;

import java.io.IOException;

/**
 * @Description websocket message util
 * @Author eotouch
 * @Date 2024/01/06 16:29
 * @Version 1.0
 */
public class WebSocketUtil<T> {

    public static void sendTextMessage(LivePoint livePoint, Object object){
        String message = JSON.toJSONString(object);
        try {
            livePoint.getWebSocketStorage().getSession().getBasicRemote().sendText(message);
        } catch (IOException exception) {
            BeeChannelException.cast("send message error");
        }
    }

}
