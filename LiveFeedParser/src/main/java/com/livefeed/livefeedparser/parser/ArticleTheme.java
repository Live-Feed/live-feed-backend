package com.livefeed.livefeedparser.parser;

import com.livefeed.livefeedparser.parser.dto.BodyDto;
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
    SPORTS(Header.SPORTS, Body.SPORTS), IT_SCIENCE(Header.TEMP, Body.TEMP), POLITICS(Header.TEMP, Body.TEMP);

    private final Header header;
    private final Body body;

    private final String OUTER_HTML = "outerHTML";

    // TODO: 2023/09/14 findElement의 경우 만약 cssSelector에서 해당하는 부분이 없는면 에러가 발생하는데 이때 pressCompanyName만 없는 경우 나머지는 추가하도록 변경 필요해보임
    // TODO: 2023/09/14 ArticleTheme에 파싱 로직을 넣는것보다는 파싱 로직을 전담하는 클래스를 하나 따로 두는게 좋아보임
    public HeaderDto parseHeader(WebDriver driver) {
        WebElement webElement = driver.findElement(By.cssSelector(header.innerHtml));

        String html = webElement.getAttribute(OUTER_HTML);
        String articleTitle = webElement.findElement(By.cssSelector(header.articleTitle)).getText();
        String pressCompanyName = webElement.findElement(By.cssSelector(header.pressCompanyName)).getAttribute("alt");
        String publicationTime = webElement.findElement(By.cssSelector(header.publicationTime)).getText();
        String originArticleUrl = webElement.findElement(By.cssSelector(header.originArticleUrl)).getAttribute("href");

        return HeaderDto.of(html, articleTitle, pressCompanyName, publicationTime, originArticleUrl);
    }

    public BodyDto parseBody(WebDriver driver) {
        WebElement webElement = driver.findElement(By.cssSelector(body.innerHtml));

        String htmlTemp = webElement.getAttribute(OUTER_HTML);
        String html = getHtml(htmlTemp);
        String journalistName = webElement.findElement(By.cssSelector(body.journalistName)).getText().split(" ")[0];
        String[] journalistEmailArray = webElement.findElement(By.cssSelector(body.journalistEmail)).getText().split(" ");
        String journalistEmail = journalistEmailArray[journalistEmailArray.length -1];

        return BodyDto.of(html, journalistName, journalistEmail);
    }

    private String getHtml(String htmlTemp) {
        int index = htmlTemp.indexOf("<div class=\"reporter_area\"");
        return htmlTemp.substring(0, index) + "</div>";
    }

    @RequiredArgsConstructor
    private enum Header {
        SPORTS("div.news_headline", "span#pressLogo img", "h4.title", "div.info span:nth-child(1)", "div.info a"),
        TEMP(null, null, null, null, null);

        private final String innerHtml;
        private final String pressCompanyName;
        private final String articleTitle;
        private final String publicationTime;
        private final String originArticleUrl;
    }

    @RequiredArgsConstructor
    private enum Body {
        SPORTS("div#newsEndContents", "p.byline", "p.byline"),
        TEMP(null, null, null);

        private final String innerHtml;
        private final String journalistName;
        private final String journalistEmail;
    }

}
