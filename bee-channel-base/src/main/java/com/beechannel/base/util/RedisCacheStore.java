package com.beechannel.base.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author eotouch
 * @version 1.0
 * @description TODO
 * @date 2023/03/16 20:59
 */
@Component("RedisCacheStore")
public class RedisCacheStore<T>{

    @Resource
    private RedisTemplate redisTemplate;

    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, Integer expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }


    public void setToList(String key, T value){
        redisTemplate.opsForSet().add(key, value);
    }

    public Set getFromList(String key){
        return redisTemplate.opsForSet().members(key);
    }

    public boolean existInList(String key, String value){
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public void removeInList(String key, T value){
        redisTemplate.opsForSet().remove(key, value);
    }
}
