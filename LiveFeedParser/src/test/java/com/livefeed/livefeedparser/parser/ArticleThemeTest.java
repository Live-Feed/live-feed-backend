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
        String url = "https://sports.news.naver.com/news?oid=109&aid=0004924910";
        driver.get(url);
        ArticleTheme sports = ArticleTheme.SPORTS;
        // when
        HeaderDto headerDto = sports.parseHeader(driver);
        driver.quit();
        // then
        log.info("headerDto = {}", headerDto);
        assertThat(headerDto.html()).isNotBlank();
        assertThat(headerDto.articleTitle()).isEqualTo("AG 기간 필승조 이탈 대비, 이강철 감독이 믿는 카드…'홀드왕' 출신이 있다");
        assertThat(headerDto.pressCompanyName()).isEqualTo("OSEN");
        assertThat(headerDto.publicationTime()).isEqualTo("기사입력 2023.09.11. 오후 02:31");
        assertThat(headerDto.originArticleUrl()).isEqualTo("http://www.osen.co.kr/article/G1112180712");
    }

    @DisplayName("parseBody 메서드에서 네이버 스포츠 기사의 본문을 제대로 파싱하는지 확인하는 테스트.")
    @Test
    void parseBodySports() {
        // given
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        String url = "https://sports.news.naver.com/news?oid=666&aid=0000021171";
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
        assertThat(bodyDto.journalistName()).isEqualTo("황선학");
        assertThat(bodyDto.journalistEmail()).isEqualTo("2hwangpo@kyeonggi.com");
    }
}