package com.livefeed.livefeedparser.kafka.consumer;

import com.livefeed.livefeedparser.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedparser.parser.ArticleTheme;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class KafkaConsumerTest {

    @DisplayName("consumer에서 받은 key의 theme 값이 SPORTS라면 타겟팅한 ArticleTheme을 찾는지 확인하는 테스트")
    @Test
    void findArticleThemeByConsumerKey() {
        // given
        ConsumerKeyDto key = new ConsumerKeyDto("NAVER", "SPORTS");
        // when
        ArticleTheme articleTheme = ArticleTheme.valueOf(key.theme());
        // then
        assertThat(articleTheme).isEqualTo(ArticleTheme.SPORTS);
    }

    @DisplayName("consumer에서 받은 key의 theme 값이 SPORTS가 아닌 경우 IllegalArgumentException에러를 발생한다.")
    @Test
    void cannotFindArticleTheme() {
        // given
        ConsumerKeyDto key = new ConsumerKeyDto("NAVER", "SPORTSS");
        // when
        // then
        assertThatThrownBy(() -> ArticleTheme.valueOf(key.theme()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}