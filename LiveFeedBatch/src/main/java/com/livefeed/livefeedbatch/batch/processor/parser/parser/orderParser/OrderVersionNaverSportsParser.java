package com.livefeed.livefeedbatch.batch.processor.parser.parser.orderParser;

import com.livefeed.livefeedbatch.batch.common.dto.keydto.Platform;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.Theme;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.BodyDto;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.HeaderDto;
import com.livefeed.livefeedbatch.batch.processor.parser.parser.NameAndEmailParser;
import com.livefeed.livefeedbatch.batch.processor.parser.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.data.util.Pair;

@Slf4j
@RequiredArgsConstructor
public class OrderVersionNaverSportsParser extends Parser {

    private final Platform platform = Platform.NAVER;
    private final Theme theme = Theme.SPORTS;

    private static final Header header = Header.SPORTS;
    private static final Body body = Body.SPORTS;


    private final NameAndEmailParser nameAndEmailParser;

    @Override
    protected HeaderDto parseHeader(WebDriver driver) {
        WebElement webElement = driver.findElement(By.cssSelector(header.innerHtml));

        String html = webElement.getAttribute(OUTER_HTML);
        String articleTitle = webElement.findElement(By.cssSelector(header.articleTitle)).getText();
        String pressCompanyName = webElement.findElement(By.cssSelector(header.pressCompanyName)).getAttribute("alt");
        String publicationTime = webElement.findElement(By.cssSelector(header.publicationTime)).getText();
        String originArticleUrl = webElement.findElement(By.cssSelector(header.originArticleUrl)).getAttribute("href");

        return HeaderDto.of(html, articleTitle, pressCompanyName, publicationTime, originArticleUrl);
    }

    @Override
    protected BodyDto parseBody(WebDriver driver) {
        WebElement webElement = driver.findElement(By.cssSelector(body.innerHtml));

        String htmlTemp = webElement.getAttribute(OUTER_HTML);
        String html = getHtml(htmlTemp);
        String journalistNameAndEmail = webElement.findElement(By.cssSelector(body.journalistNameAndEmail)).getText();
        Pair<String, String> nameEmailPair = nameAndEmailParser.extractNameAndEmail(journalistNameAndEmail);

        return BodyDto.of(html, nameEmailPair.getFirst(), nameEmailPair.getSecond());
    }

    @Override
    public boolean support(UrlInfo key) {
        return key.platform() == platform && key.theme() == theme;
    }

    private String getHtml(String htmlTemp) {
        int index = htmlTemp.indexOf("<div class=\"reporter_area\"");
        return htmlTemp.substring(0, index) + "</div>";
    }

    @RequiredArgsConstructor
    private enum Header {
        SPORTS("div.news_headline", "span#pressLogo img", "h4.title", "div.info span:nth-child(1)", "div.info a");

        private final String innerHtml;
        private final String pressCompanyName;
        private final String articleTitle;
        private final String publicationTime;
        private final String originArticleUrl;
    }

    @RequiredArgsConstructor
    private enum Body {
        SPORTS("div#newsEndContents", "p.byline");

        private final String innerHtml;
        private final String journalistNameAndEmail;
    }
}
