package com.beechannel.live.listener;

import com.beechannel.base.constant.RabbitMqValue;
import com.beechannel.base.util.RedisCacheStore;
import com.beechannel.live.domain.dto.LiveMessage;
import com.beechannel.live.endpoint.LivePoint;
import com.beechannel.live.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

import static com.beechannel.live.constant.RedisStoreSpaceKey.LIVE_ROOM_STORE_SPACE_KEY;

/**
 * @Description rabbit-mq exchange and queue configuration
 * @Author eotouch
 * @Date 2024/01/06 20:52
 * @Version 1.0
 */
@Slf4j
@Configuration
public class RabbitMqBindingListener {

    @Resource
    private RedisCacheStore redisCacheStore;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = RabbitMqValue.LIVE_MESSAGE_QUEUE),
                    exchange = @Exchange(name = RabbitMqValue.LIVE_MESSAGE_EXCHANGE),
                    key = {RabbitMqValue.LIVE_MESSAGE_KEY}
            )
    )
    public void liveMessageQueue(LiveMessage liveMessage){
        String roomId = liveMessage.getRoomId();
        Map<String, LivePoint> userLivePoint = LivePoint.getUserLivePoint();
        Set<String> userInRoom = redisCacheStore.getFromSet(LIVE_ROOM_STORE_SPACE_KEY + roomId);
        userInRoom.forEach(userId -> {
            boolean exist = userLivePoint.containsKey(userId);
            if(exist){
                LivePoint livePoint = userLivePoint.get(userId);
                WebSocketUtil.sendTextMessage(livePoint, liveMessage);
            }
        });
    }
}
