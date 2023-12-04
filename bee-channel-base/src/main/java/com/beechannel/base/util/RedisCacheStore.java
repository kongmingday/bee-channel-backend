package com.beechannel.base.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author eotouch
 * @version 1.0
 * @description TODO
 * @date 2023/03/16 20:59
 */
@Component("RedisCacheStore")
public class RedisCacheStore{

    @Resource
    private RedisTemplate redisTemplate;

    public void set(String key, String value, Integer expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
