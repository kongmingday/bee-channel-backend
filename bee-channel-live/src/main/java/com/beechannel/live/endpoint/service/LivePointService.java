package com.beechannel.live.endpoint.service;

import com.beechannel.live.endpoint.LivePoint;

/**
 * @Description live point service
 * @Author eotouch
 * @Date 2024/01/06 17:05
 * @Version 1.0
 */
public interface LivePointService {

    void checkOnOpen(String roomId);

    void addUserToRoom(LivePoint livePoint);

    void removeUserInRoom(LivePoint livePoint);

    void sendUserMessage(String message);
}
