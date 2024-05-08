package com.livefeed.livefeedservice.newarticle.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

class RedisUserKeywordRepositoryTest {

    @Test
    @DisplayName("키워드를 등록했을때 변화가 없는 경우 0을 반환합니다.")
    void returnZero() {
        // given
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();

        UserKeywordRepository userKeywordRepository = new RedisUserKeywordRepository(redisTemplate);
        // when
        Long first = userKeywordRepository.setUserKeywords("testKey", List.of("key1", "key2"));
        Long second = userKeywordRepository.setUserKeywords("testKey", List.of("key1", "key2"));
        // then
        Assertions.assertThat(first).isEqualTo(2);
        Assertions.assertThat(second).isEqualTo(0);
    }
}