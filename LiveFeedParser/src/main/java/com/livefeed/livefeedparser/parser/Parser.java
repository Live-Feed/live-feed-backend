package com.livefeed.livefeedparser.parser;

import com.livefeed.livefeedparser.parser.dto.BodyDto;
import com.livefeed.livefeedparser.parser.dto.HeaderDto;
import com.livefeed.livefeedparser.parser.dto.ParseResultDto;
import com.livefeed.livefeedparser.parser.exception.ArticleParsingException;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class Parser {

    public ParseResultDto parseArticle(String url, ArticleTheme articleTheme) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get(url);
            HeaderDto headerDto = articleTheme.parseHeader(driver);
            BodyDto bodyDto = articleTheme.parseBody(driver);
            return ParseResultDto.from(headerDto, bodyDto);
        } catch (Exception e) {
            log.error("신문 기사 파싱 도중 에러가 발생했습니다. ", e);
            throw new ArticleParsingException(e.getMessage());
        } finally {
            driver.quit();
        }
    }

    private HeaderDto parseHeader(String url, ArticleTheme articleTheme) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(url);
            return articleTheme.parseHeader(driver);
        } finally {
            driver.quit();
        }
    }

    private BodyDto parseBody(String url, ArticleTheme articleTheme) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(url);
            return articleTheme.parseBody(driver);
        } finally {
            driver.quit();
        }
    }
}
