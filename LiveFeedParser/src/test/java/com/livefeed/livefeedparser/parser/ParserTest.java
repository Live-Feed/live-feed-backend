package com.livefeed.livefeedparser.parser;

import com.livefeed.livefeedparser.parser.dto.HeaderDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class ParserTest {

    @DisplayName("WebDriver 가 원하는 페이지를 받아오는지 확인하는 테스트")
    @Test
    void getArticleHtml() throws InterruptedException {
        // given
        Parser parser = new Parser();
        String url = "https://sports.news.naver.com/news?oid=109&aid=0004924910";
        // when
        HeaderDto headerDto = parser.parseHeader(url);

        assertThat(headerDto.outerHtml()).isNotBlank();
        log.info("outerHtml = {}", headerDto.outerHtml());
        log.info("title = {}", headerDto.articleTitle());
        log.info("createdAt = {}", headerDto.createdAt());
        log.info("publicationTime = {}", headerDto.publicationTime());
        log.info("originArticleUrl = {}", headerDto.originArticleUrl());
    }
}