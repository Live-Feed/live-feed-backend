package com.livefeed.livefeedparser.parser.parser;

import com.livefeed.livefeedparser.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedparser.parser.Platform;
import com.livefeed.livefeedparser.parser.Theme;
import com.livefeed.livefeedparser.parser.dto.BodyDto;
import com.livefeed.livefeedparser.parser.dto.HeaderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class NaverSportsParser extends Parser {

    private final Platform platform = Platform.NAVER;
    private final Theme theme = Theme.SPORTS;

    private final List<Header> headers = List.of(Header.values());
    private final List<Body> bodies = List.of(Body.values());

    @Override
    protected HeaderDto parseHeader(WebDriver driver) {
        for (Header header : headers) {
            try {
                WebElement webElement = driver.findElement(By.cssSelector(header.innerHtml));

                String html = webElement.getAttribute(OUTER_HTML);
                String articleTitle = webElement.findElement(By.cssSelector(header.articleTitle)).getText();
                String pressCompanyName = webElement.findElement(By.cssSelector(header.pressCompanyName)).getAttribute("alt");
                String publicationTime = webElement.findElement(By.cssSelector(header.publicationTime)).getText();
                String originArticleUrl = webElement.findElement(By.cssSelector(header.originArticleUrl)).getAttribute("href");

                return HeaderDto.of(html, articleTitle, pressCompanyName, publicationTime, originArticleUrl);
            } catch (Exception e) {
                log.warn("맞지 않은 형식의 Header cssSelector 입니다. header = {}", header);
            }
        }
        return null;
    }

    @Override
    protected BodyDto parseBody(WebDriver driver) {
        for (Body body : bodies) {
            try {
                WebElement webElement = driver.findElement(By.cssSelector(body.innerHtml));

                String htmlTemp = webElement.getAttribute(OUTER_HTML);
                String html = getHtml(htmlTemp);
                String journalistName = webElement.findElement(By.cssSelector(body.journalistName)).getText().split(" ")[0];
                String[] journalistEmailArray = webElement.findElement(By.cssSelector(body.journalistEmail)).getText().split(" ");
                String journalistEmail = journalistEmailArray[journalistEmailArray.length - 1];

                return BodyDto.of(html, journalistName, journalistEmail);
            } catch (Exception e) {
                log.warn("맞지 않은 형식의 Body cssSelector 입니다. body = {}", body);
            }
        }
        return null;
    }

    @Override
    public boolean support(ConsumerKeyDto key) {
        return key.platform() == platform && key.theme() == theme;
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
