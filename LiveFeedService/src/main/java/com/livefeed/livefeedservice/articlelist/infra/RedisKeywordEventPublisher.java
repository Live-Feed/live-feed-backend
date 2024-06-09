package com.livefeed.livefeedservice.articlelist.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedservice.articlelist.domain.repository.KeywordEventPublisher;
import com.livefeed.livefeedservice.articlelist.dto.KeywordEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisKeywordEventPublisher implements KeywordEventPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.redis-pub-sub.keyword-channel}")
    private String keywordChannelTopic;

    @Override
    public void publishKeywordEvent(KeywordEvent keywordEvent) {
        try {
            Long count = redisTemplate.convertAndSend(ChannelTopic.of(keywordChannelTopic).getTopic(), objectMapper.writeValueAsString(keywordEvent.keywords()));
            log.info("success publish new user keyword count = {}", count);
        } catch (Exception e) {
            log.error("Failed to publish new user keyword to redis ", e);
            throw new RuntimeException(e);
        }
    }
}
