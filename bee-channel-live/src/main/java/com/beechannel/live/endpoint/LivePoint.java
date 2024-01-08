package com.beechannel.live.endpoint;

import com.beechannel.live.config.WebSocketConfig;
import com.beechannel.live.domain.bo.WebSocketStorage;
import com.beechannel.live.endpoint.service.LivePointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description websocket live
 * @Author eotouch
 * @Date 2024/01/05 17:36
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/room/{roomId}/{userId}", configurator = WebSocketConfig.class)
@Component
public class LivePoint  {

    private static Map<String, LivePoint> userLivePonit = new ConcurrentHashMap<>();

    private WebSocketStorage webSocketStorage;

    private static LivePointService livePointService;

    public static Map<String, LivePoint> getUserLivePoint(){
        return LivePoint.userLivePonit;
    }

    public WebSocketStorage getWebSocketStorage() {
        return webSocketStorage;
    }

    @Autowired
    public void setLivePointService(LivePointService livePointService) {
        LivePoint.livePointService = livePointService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId, @PathParam("userId") String userId){
        webSocketStorage = new WebSocketStorage();

        livePointService.checkOnOpen(roomId);
        this.webSocketStorage.setRoomId(roomId);

        boolean isAnonymous = !StringUtils.hasLength(userId);

        if(isAnonymous){
            userId = UUID.randomUUID().toString();
        }

        this.webSocketStorage.setAnonymous(isAnonymous);
        this.webSocketStorage.setUserId(userId);
        this.webSocketStorage.setSession(session);

        userLivePonit.put(userId, this);
        livePointService.addUserToRoom(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        livePointService.sendUserMessage(message);
    }

    @OnError
    public void onError(Session session, Throwable e) throws IOException {
        log.info("websocket消息: 出现系统错误 ");
        e.printStackTrace();
        session.close();
    }

    @OnClose
    public void OnClose(Session session) throws IOException {
        livePointService.removeUserInRoom(this);
        userLivePonit.remove(this.webSocketStorage.getUserId());
        session.close();
    }

}
