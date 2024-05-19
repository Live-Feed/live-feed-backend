package com.livefeed.livefeedservice.articlelist.service;

import com.livefeed.livefeedservice.articlelist.dto.KeywordEvent;
import com.livefeed.livefeedservice.newarticle.domain.Emitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordEventListener {

    private static final String SORTED_SET_KEY = "keywordRank";

    private final RedisTemplate<String, String> redisTemplate;
    private final Emitters emitters;

    @Async
    @EventListener
    public void processKeywordEvent(KeywordEvent keywordEvent) {
        log.info("keywordEvent = {}", keywordEvent);
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        keywordEvent.keywords()
                .forEach(keyword -> zSetOperations.incrementScore(SORTED_SET_KEY, keyword, 1));

        Set<String> keywordsRanking = zSetOperations.reverseRange(SORTED_SET_KEY, 0, 9);
        // TODO: 5/17/24 기존 랭킹과 동일하면 보낼지 말지 논의 후 수정 필요
        log.info("keywordsRanking = {}", keywordsRanking);
        emitters.sendKeywordRankingMessage(keywordsRanking);
    }
}
