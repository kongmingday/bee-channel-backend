package com.beechannel.live.endpoint.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.base.constant.RabbitMqValue;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.RedisCacheStore;
import com.beechannel.live.domain.bo.WebSocketStorage;
import com.beechannel.live.domain.dto.LiveMessage;
import com.beechannel.live.domain.po.Live;
import com.beechannel.live.endpoint.LivePoint;
import com.beechannel.live.endpoint.service.LivePointService;
import com.beechannel.live.feign.UserClient;
import com.beechannel.live.service.LiveService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;

import static com.beechannel.live.constant.RedisStoreSpaceKey.LIVE_ROOM_STORE_SPACE_KEY;

/**
 * @Description live point service implement
 * @Author eotouch
 * @Date 2024/01/06 17:05
 * @Version 1.0
 */
@Service
public class LivePointServiceImpl implements LivePointService, Serializable {

    private static final long serialVersionUID = 1L;

    @Resource
    private LiveService liveService;

    @Resource
    private RedisCacheStore redisCacheStore;

    @Resource
    private UserClient userClient;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * @description check the roomId
     * @param roomId
     * @return void
     * @author eotouch
     * @date 2024-01-06 21:56
     */
    @Override
    public void checkOnOpen(String roomId) {
        if(!StringUtils.hasText(roomId)){
            BeeChannelException.cast("房间号有误");
        }

        LambdaQueryWrapper<Live> liveQuery = new LambdaQueryWrapper<>();
        liveQuery.eq(Live::getLiveKey, roomId);
        boolean existRoom = liveService.count(liveQuery) > 0;
        if(!existRoom){
            BeeChannelException.cast("房间号有误");
        }
    }

    /**
     * @description add user to room
     * @param livePoint websocket point
     * @return void
     * @author eotouch
     * @date 2024-01-06 21:57
     */
    @Override
    public void addUserToRoom(LivePoint livePoint) {
        WebSocketStorage webSocketStorage = livePoint.getWebSocketStorage();
        boolean isAnonymous = webSocketStorage.isAnonymous();

        if(isAnonymous){
           return;
        }

        String userId = webSocketStorage.getUserId();
        String roomId = webSocketStorage.getRoomId();
        String liveRoomKey = LIVE_ROOM_STORE_SPACE_KEY + roomId;
        boolean exist = redisCacheStore.existInList(liveRoomKey, userId);
        if(exist){
            return;
        }
        redisCacheStore.setToList(liveRoomKey, userId);

        // make live message
        LiveMessage liveMessage = new LiveMessage();
        setLiveMessageByUser(userId, liveMessage);
        liveMessage.setUserId(userId);
        liveMessage.setRoomId(roomId);
        liveMessage.setSystem(true);

        // add user to redis, and send message by rabbit-mq
        rabbitTemplate.convertAndSend(
                RabbitMqValue.LIVE_MESSAGE_EXCHANGE,
                RabbitMqValue.LIVE_MESSAGE_KEY,
                liveMessage
        );
    }

    /**
     * @description remove user in room
     * @param livePoint
     * @return void
     * @author eotouch
     * @date 2024-01-06 21:58
     */
    @Override
    public void removeUserInRoom(LivePoint livePoint) {
        WebSocketStorage webSocketStorage = livePoint.getWebSocketStorage();
        String liveRoomKey = LIVE_ROOM_STORE_SPACE_KEY + webSocketStorage.getRoomId();
        redisCacheStore.removeInList(liveRoomKey, webSocketStorage.getUserId());
    }

    /**
     * @description set user message
     * @param message
     * @return void
     * @author eotouch
     * @date 2024-01-08 0:35
     */
    @Override
    public void sendUserMessage(String message) {
        LiveMessage liveMessage = JSON.parseObject(message, LiveMessage.class);
        String targetMessage = liveMessage.getMessage();
        if(!StringUtils.hasText(targetMessage)){
            return;
        }
        rabbitTemplate.convertAndSend(
                RabbitMqValue.LIVE_MESSAGE_EXCHANGE,
                RabbitMqValue.LIVE_MESSAGE_KEY,
                liveMessage
        );
    }

    private void setLiveMessageByUser(String userId, LiveMessage liveMessage){
        RestResponse<User> userInfo = userClient.getUserInfo(Long.valueOf(userId));
        User result = userInfo.getResult();
        if(userInfo.getCode() == 200 && result != null){
            liveMessage.setProfile(result.getProfile());
            liveMessage.setUsername(result.getUsername());
            liveMessage.setMessage(result.getUsername() + "entered the studio.");
        }else{
            BeeChannelException.cast("Enter to studio has error");
        }
    }

}
