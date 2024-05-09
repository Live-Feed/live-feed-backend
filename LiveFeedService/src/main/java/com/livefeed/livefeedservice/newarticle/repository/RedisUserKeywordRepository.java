package com.livefeed.livefeedservice.newarticle.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    public int updateUserKeywords(String sseKey, List<String> newKeywords) {
        int changeWordsCount = 0;

        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Set<String> existedKeywords = getUserKeywords(sseKey);

        if (existedKeywords.isEmpty() && newKeywords.isEmpty()) {
            return 0;
        }

        if (existedKeywords.isEmpty()) {
            return Objects.requireNonNull(setOperations.add(sseKey, newKeywords.toArray(new String[0]))).intValue();
        }

        changeWordsCount += addKeywordsCount(sseKey, newKeywords, setOperations, existedKeywords);
        changeWordsCount += removeKeywordsCount(sseKey, newKeywords, setOperations, existedKeywords);

        return changeWordsCount;
    }

    @Override
    public void deleteUserKeywords(String sseKey) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Set<String> existedKeywords = getUserKeywords(sseKey);
        if (!existedKeywords.isEmpty()) {
            setOperations.remove(sseKey, (Object[]) existedKeywords.toArray(new String[0]));
        }
    }

    private int addKeywordsCount(String sseKey, List<String> keywords, SetOperations<String, String> setOperations, Set<String> existedKeywords) {
        Set<String> toAddKeywords = new HashSet<>(keywords);
        toAddKeywords.removeAll(existedKeywords);

        if (!toAddKeywords.isEmpty()) {
            return Objects.requireNonNull(setOperations.add(sseKey, toAddKeywords.toArray(new String[0]))).intValue();
        }
        return 0;
    }

    private int removeKeywordsCount(String sseKey, List<String> keywords, SetOperations<String, String> setOperations, Set<String> existedKeywords) {
        Set<String> toRemoveKeywords = new HashSet<>(existedKeywords);
        keywords.forEach(toRemoveKeywords::remove);

        if (!toRemoveKeywords.isEmpty()) {
            return Objects.requireNonNull(setOperations.remove(sseKey, (Object[]) toRemoveKeywords.toArray(new String[0]))).intValue();
        }
        return 0;
    }
}
