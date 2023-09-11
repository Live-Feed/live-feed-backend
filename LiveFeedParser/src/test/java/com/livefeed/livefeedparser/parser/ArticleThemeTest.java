package com.livefeed.livefeedparser.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ArticleThemeTest {

    @DisplayName("html 클래스 태그를 . 을 기준으로 제대로 구별하는지 확인하는 메서드")
    @Test
    void getSrcValueByClass() {
        // given
        String source = "class.news_headline";
        // when
        By srcValue = ArticleTheme.getSrcValue(source);
        // then
        By.ByClassName result = (By.ByClassName) srcValue;

        assertThat(result.toString()).isEqualTo("By.className: news_headline");
    }
}