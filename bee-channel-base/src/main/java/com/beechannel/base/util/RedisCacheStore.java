package com.beechannel.base.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author eotouch
 * @version 1.0
 * @description TODO
 * @date 2023/03/16 20:59
 */
@Component("RedisCacheStore")
public class RedisCacheStore<T> {

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


    public void setToSet(String key, T value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Set getFromSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public void addToList(String key, T value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public List getFromList(String key, Long pageNo, Long pageSize) {
        long start = (pageNo - 1) * pageSize;
        return redisTemplate.opsForList().range(key, start, start + pageSize);
    }

    public void removeInList(String key, T value) {
        redisTemplate.opsForList().remove(key, 1, value);
    }

    public boolean existInSet(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public void removeInSet(String key, T value) {
        redisTemplate.opsForSet().remove(key, value);
    }
}
