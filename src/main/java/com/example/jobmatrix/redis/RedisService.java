package com.example.jobmatrix.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String,String> redisTemplate;

    public void save(
            String key,
            String value,
            long minutes
    ) {
        redisTemplate.opsForValue()
                .set(
                        key,
                        value,
                        Duration.ofMinutes(minutes)
                );
    }

    public String get(String key) {
        return redisTemplate.opsForValue()
                .get(key);
    }
}