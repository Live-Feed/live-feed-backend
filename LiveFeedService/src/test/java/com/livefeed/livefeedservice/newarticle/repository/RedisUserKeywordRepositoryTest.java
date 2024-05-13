package com.livefeed.livefeedservice.newarticle.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Disabled
class RedisUserKeywordRepositoryTest {

    private static UserKeywordRepository userKeywordRepository;
    private final String sseKey = "sseKey";

    @BeforeAll
    static void setUp() {
        System.out.println("test class start");
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();

        userKeywordRepository = new RedisUserKeywordRepository(redisTemplate);
    }
    
    @AfterEach
    void tearDown() {
        userKeywordRepository.deleteUserKeywords(sseKey);
    }

    @DisplayName("기존에 등록된 키워드가 없고 새로 등록할 키워드도 없는 경우 0을 반환합니다.")
    @Test
    void noKeyWords() {
        // given
        // when
        int result = userKeywordRepository.updateUserKeywords(sseKey, List.of());
        // then
        Assertions.assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("키워드를 등록했을때 변화가 없는 경우 0을 반환합니다.")
    void returnZero() {
        // when
        int first = userKeywordRepository.updateUserKeywords(sseKey, List.of("key1", "key2"));
        int second = userKeywordRepository.updateUserKeywords(sseKey, List.of("key1", "key2"));
        // then
        Assertions.assertThat(first).isEqualTo(2);
        Assertions.assertThat(second).isEqualTo(0);
    }

    @DisplayName("기존 키워드에서 한 개의 키워드가 사라진 경우 사라진 개수를 반환합니다.")
    @Test
    void setUserKeyword() {
        // given
        // when
        int first = userKeywordRepository.updateUserKeywords(sseKey, List.of("key1", "key2"));
        int second = userKeywordRepository.updateUserKeywords(sseKey, List.of("key1"));
        // then
        Assertions.assertThat(first).isEqualTo(2);
        Assertions.assertThat(second).isEqualTo(1);
    }

    @DisplayName("기존 키워드가 존재하며 새로 등록하는 키워드가 없는 경우 사라진 개수를 반환합니다.")
    @Test
    void setUserKeywordRemove() {
        // given
        // when
        int first = userKeywordRepository.updateUserKeywords(sseKey, List.of("key1", "key2"));
        int second = userKeywordRepository.updateUserKeywords(sseKey, List.of());

        // then
        Assertions.assertThat(first).isEqualTo(2);
        Assertions.assertThat(second).isEqualTo(2);
    }

    @DisplayName("기존 키워드와 하나만 동일하고 나머지가 다른 키워드를 등록하는 경우")
    @Test
    void setUserKeywordAnother() {
        // given
        // when
        int first = userKeywordRepository.updateUserKeywords(sseKey, List.of("key1", "key2"));
        int second = userKeywordRepository.updateUserKeywords(sseKey, List.of("key1", "key3", "key4"));

        // then
        Assertions.assertThat(first).isEqualTo(2);
        Assertions.assertThat(second).isEqualTo(3);
    }
}