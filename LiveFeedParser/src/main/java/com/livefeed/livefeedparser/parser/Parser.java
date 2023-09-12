package com.livefeed.livefeedparser.parser;

import com.livefeed.livefeedparser.parser.dto.HeaderDto;
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

    public HeaderDto parseHeader(String url, ArticleTheme articleTheme) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(url);
            return articleTheme.parseHeader(driver);
        } finally {
            driver.quit();
        }
    }
}
