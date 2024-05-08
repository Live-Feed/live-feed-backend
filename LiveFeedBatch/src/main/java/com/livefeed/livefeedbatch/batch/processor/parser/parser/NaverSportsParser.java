package com.livefeed.livefeedbatch.batch.processor.parser.parser;

import com.livefeed.livefeedbatch.batch.common.dto.keydto.Platform;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.Theme;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.BodyDto;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.HeaderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverSportsParser extends Parser {

    private static final Platform platform = Platform.NAVER;
    private static final Theme theme = Theme.SPORTS;

    private final NameAndEmailParser nameAndEmailParser;

    @Override
    protected HeaderDto parseHeader(WebDriver driver) {
        return Header.SPORTS.parseHeader(driver);
    }

    @Override
    protected BodyDto parseBody(WebDriver driver) {
        WebElement webElement = driver.findElement(By.cssSelector(Body.SPORTS.innerHtml));

        String html = getHtml(webElement);
        String journalistNameAndEmail = webElement.findElement(By.cssSelector(Body.SPORTS.journalistNameAndEmail)).getText();
        Pair<String, String> nameEmailPair = nameAndEmailParser.extractNameAndEmail(journalistNameAndEmail);

        return BodyDto.of(html, nameEmailPair.getFirst(), nameEmailPair.getSecond());
    }

    @Override
    public boolean support(UrlInfo key) {
        return key.platform() == platform && key.theme() == theme;
    }

    private String getHtml(WebElement webElement) {
        return webElement.findElement(By.cssSelector("#comp_news_article")).getAttribute(OUTER_HTML);
    }

    @RequiredArgsConstructor
    private enum Header {
        SPORTS("#content div.NewsEndMain_comp_article_head__Uqd6M", "a.NewsEndMain_article_head_press_logo__BrqAh img", "h2.NewsEndMain_article_title__kqEzS", "em.NewsEndMain_date__xjtsQ", "div.NewsEndMain_article_head_date_info__jGlzH a");

        private final String innerHtml;
        private final String pressCompanyName;
        private final String articleTitle;
        private final String publicationTime;
        private final String originArticleUrl;

        private HeaderDto parseHeader(WebDriver webDriver) {
            WebElement webElement = webDriver.findElement(By.cssSelector(innerHtml));
            String html = htmlHeader(webElement);
            String articleTitle = webElement.findElement(By.cssSelector(this.articleTitle)).getText();
            String pressCompanyName = webElement.findElement(By.cssSelector(this.pressCompanyName)).getAttribute("alt");
            String publicationTime = webElement.findElement(By.cssSelector(this.publicationTime)).getText();
            String originArticleUrl = webElement.findElement(By.cssSelector(this.originArticleUrl)).getAttribute("href");

            return HeaderDto.of(html, articleTitle, pressCompanyName, publicationTime, originArticleUrl);
        }

        private String htmlHeader(WebElement webElement) {
            String headerLogoHtml = webElement.findElement(By.cssSelector("a.NewsEndMain_article_head_press_logo__BrqAh")).getAttribute(OUTER_HTML);
            String titleHtml = webElement.findElement(By.cssSelector("div.NewsEndMain_article_head_title__ztaL4")).getAttribute(OUTER_HTML);
            String dateInfoHtml  = webElement.findElement(By.cssSelector("div.NewsEndMain_article_head_date_info__jGlzH")).getAttribute(OUTER_HTML);
            return "<div>" + headerLogoHtml + titleHtml + dateInfoHtml + "</div>";
        }
    }

    @RequiredArgsConstructor
    private enum Body {
        SPORTS("#content", "div.NewsEndMain_article_journalist_info__Cdr3D");

        private final String innerHtml;
        private final String journalistNameAndEmail;
    }
}
