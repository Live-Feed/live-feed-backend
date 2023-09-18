package com.livefeed.livefeedparser.parser;

import com.livefeed.livefeedparser.parser.dto.BodyDto;
import com.livefeed.livefeedparser.parser.dto.HeaderDto;
import com.livefeed.livefeedparser.parser.dto.ParseResultDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class ParserTest {

    @Autowired
    private Parser parser;

    @DisplayName("Parser 클래스에서 기사 url을 통해 기사를 크롤링하여 ParseResultDto 클래스를 전달한다.")
    @Test
    void parseArticle() {
        // given
        String url = "https://sports.news.naver.com/news?oid=658&aid=0000052259";
        // when
        ParseResultDto result = parser.parseArticle(url, ArticleTheme.SPORTS);
        // then
        assertThat(result.articleTitle()).isNotBlank();
        assertThat(result.publicationTime()).isNotBlank();
        assertThat(result.pressCompanyName()).isNotBlank();
        assertThat(result.journalistName()).isNotBlank();
        assertThat(result.journalistEmail()).isNotBlank();
        assertThat(result.originArticleUrl()).isNotBlank();
        assertThat(result.html()).isNotBlank();
    }
}