package com.livefeed.livefeedparser.parser;

import com.livefeed.livefeedparser.parser.dto.HeaderDto;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.assertj.core.api.Assertions.*;

class ArticleThemeTest {

    @DisplayName("parseHeader에서 스포츠 기사를 제대로 파싱하는지 확인하는 테스트")
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
        assertThat(headerDto.innerHtml()).isNotBlank();
        assertThat(headerDto.articleTitle()).isEqualTo("AG 기간 필승조 이탈 대비, 이강철 감독이 믿는 카드…'홀드왕' 출신이 있다");
        assertThat(headerDto.publicationTime()).isEqualTo("기사입력 2023.09.11. 오후 02:31");
        assertThat(headerDto.originArticleUrl()).isEqualTo("http://www.osen.co.kr/article/G1112180712");
    }
}