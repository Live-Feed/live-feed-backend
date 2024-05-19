package com.livefeed.livefeedservice.articlelist.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisKeywordRankRepository implements KeywordRankRepository {

    private static final String SORTED_SET_KEY = "keywordRank";
    private static final int FIRST_KEYWORD_INDEX = 0;
    private static final int LAST_KEYWORD_INDEX = 9;
    private static final String RANK_SET_KEY = "keywordRankSet";


    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void updateKeywordScore(List<String> keywords) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        keywords.forEach(keyword -> zSetOperations.incrementScore(SORTED_SET_KEY, keyword, 1));
    }

    @Override
    public Set<String> getTopRankKeywords() {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.reverseRange(SORTED_SET_KEY, FIRST_KEYWORD_INDEX, LAST_KEYWORD_INDEX);
    }

    @Override
    public Set<String> getExistedTopRankKeywords() {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.members(RANK_SET_KEY);
    }

    @Override
    public void updateTopRankKeywords(Set<String> updatedTopRankKeywords) {
        redisTemplate.delete(RANK_SET_KEY);
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.add(RANK_SET_KEY, Objects.requireNonNull(updatedTopRankKeywords).toArray(new String[0]));
    }
}
