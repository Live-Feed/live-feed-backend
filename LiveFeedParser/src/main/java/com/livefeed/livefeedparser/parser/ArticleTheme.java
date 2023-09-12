package com.livefeed.livefeedparser.parser;

import com.livefeed.livefeedparser.parser.dto.HeaderDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Getter
@RequiredArgsConstructor
public enum ArticleTheme {

    // 스포츠, IT/과학, 정치
    SPORTS(Header.SPORTS), IT_SCIENCE(Header.TEMP), POLITICS(Header.TEMP);

    private final Header header;
    private final String INNER_HTML = "innerHTML";

    public HeaderDto parseHeader(WebDriver driver) {
        WebElement webElement = driver.findElement(By.cssSelector(header.innerHtml));

        String innerHTML = webElement.getAttribute(INNER_HTML);
        String articleTitle = webElement.findElement(By.cssSelector(header.articleTitle)).getText();
        String publicationTime = webElement.findElement(By.cssSelector(header.publicationTime)).getText();
        String originArticleUrl = webElement.findElement(By.cssSelector(header.originArticleUrl)).getAttribute("href");

        // TODO: 2023/09/12 journalistName 처리 필요
        return HeaderDto.of(innerHTML, articleTitle, publicationTime, originArticleUrl, null);
    }

//    public BodyDto parseBody(WebDriver driver, String url) {
//
//    }
    
    @RequiredArgsConstructor
    private enum Header {
        SPORTS("div.news_headline", "h4.title", "div.info span:nth-child(1)",
                "div.info a", null),

        TEMP(null, null, null,
                 null, null);

        private final String innerHtml;
        private final String articleTitle;
        private final String publicationTime;
        private final String originArticleUrl;
        private final String journalistName;
    }

    @RequiredArgsConstructor
    private enum Body {
        SPORTS,
        TEMP;
    }

}
