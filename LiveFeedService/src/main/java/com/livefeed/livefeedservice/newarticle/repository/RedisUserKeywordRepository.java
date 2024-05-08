package com.livefeed.livefeedservice.newarticle.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisUserKeywordRepository implements UserKeywordRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Set<String> getUserKeywords(String sseKey) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.members(sseKey);
    }

    @Override
    public Long setUserKeywords(String sseKey, List<String> keywords) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();

        if (keywords != null && !keywords.isEmpty()) {
            String[] keywordArray = keywords.toArray(new String[0]);
            return setOperations.add(sseKey, keywordArray);
        }
        return 0L;
    }
}
