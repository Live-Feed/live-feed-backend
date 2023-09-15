package com.livefeed.livefeedparser.parser;

import com.livefeed.livefeedparser.parser.dto.BodyDto;
import com.livefeed.livefeedparser.parser.dto.HeaderDto;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class ArticleThemeTest {

    @DisplayName("parseHeader에서 네이버 스포츠 기사의 헤더를 제대로 파싱하는지 확인하는 테스트")
    @Test
    void parseHeaderSports() {
        // given
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        String url = "https://sports.news.naver.com/news?oid=658&aid=0000052259";
        driver.get(url);
        ArticleTheme sports = ArticleTheme.SPORTS;
        // when
        HeaderDto headerDto = sports.parseHeader(driver);
        driver.quit();
        // then
        log.info("headerDto = {}", headerDto);
        assertThat(headerDto.html()).isNotBlank();
        assertThat(headerDto.articleTitle()).isEqualTo("롯데 PS 진출 희박에도…\"확률 없는 건 아니니 최선\"");
        assertThat(headerDto.pressCompanyName()).isEqualTo("국제신문");
        assertThat(headerDto.publicationTime()).isEqualTo("기사입력 2023.09.12. 오후 04:52");
        assertThat(headerDto.originArticleUrl()).isEqualTo("http://www.kookje.co.kr/news2011/asp/newsbody.asp?code=0600&key=20230912.99099003450");
    }

    @DisplayName("parseBody 메서드에서 네이버 스포츠 기사의 본문을 제대로 파싱하는지 확인하는 테스트.")
    @Test
    void parseBodySports() {
        // given
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        String url = "https://sports.news.naver.com/news?oid=658&aid=0000052259";
        driver.get(url);
        ArticleTheme sports = ArticleTheme.SPORTS;
        // when
        BodyDto bodyDto;
        try {
            bodyDto = sports.parseBody(driver);
        } finally {
            driver.quit();
        }

        // then
        log.info("bodyDto = {}", bodyDto);
        assertThat(bodyDto.html()).isNotBlank();
        assertThat(bodyDto.journalistName()).isEqualTo("백창훈");
        assertThat(bodyDto.journalistEmail()).isEqualTo("huni@kookje.co.kr");
    }
}