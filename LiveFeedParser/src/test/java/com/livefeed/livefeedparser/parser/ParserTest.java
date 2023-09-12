package com.livefeed.livefeedparser.parser;

import com.livefeed.livefeedparser.parser.dto.HeaderDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class ParserTest {

    @DisplayName("스포츠 기사의 헤더를 제대로 파싱하는지 확인하는 테스트, 스포츠 기사는 journalistName = null 입니다.")
    @Test
    void getHeaderAboutSportsArticle() {
        // given
        Parser parser = new Parser();
        String url = "https://sports.news.naver.com/news?oid=658&aid=0000052259";
        // when
        HeaderDto headerDto = parser.parseHeader(url, ArticleTheme.SPORTS);
        // then
        log.info("headerDto = {}", headerDto);
        assertThat(headerDto.innerHtml()).isNotBlank();
        assertThat(headerDto.articleTitle()).isNotBlank();
        assertThat(headerDto.publicationTime()).isNotBlank();
        assertThat(headerDto.originArticleUrl()).isNotBlank();
        assertThat(headerDto.journalistName()).isNull();
    }
}