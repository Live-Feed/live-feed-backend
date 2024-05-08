package com.livefeed.livefeedbatch.batch.common.redis.operations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisOperations<K, V> {
    private final RedisTemplate<K, V> redisTemplate;

    public static final int DEFAULT_EXPIRE_DAYS = 1;

    public boolean getOrSet(K key, V value) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(existingValue -> true)
                .orElseGet(() -> {
                    set(key, value);
                    return false;
                });
    }

    private void set(K key, V value) {
        redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE_DAYS, TimeUnit.DAYS);
    }
}
