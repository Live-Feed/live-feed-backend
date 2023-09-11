package com.livefeed.livefeedparser.parser;

import com.livefeed.livefeedparser.parser.dto.HeaderDto;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Getter
@Component
public class Parser {

    public HeaderDto parseHeader(String url) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        driver.get(url);

        String outerHtmlTag = ArticleTheme.SPORTS.getHeader().getOuterHtml();
        WebElement webElement = driver.findElement(ArticleTheme.getSrcValue(outerHtmlTag));

        String outerHtml = webElement.getAttribute("innerHTML");

        String articleTitle = webElement.findElement(By.cssSelector("h4.title")).getText();

//        String articleTitle = webElement.findElement(ArticleTheme.getSrcValue("class.title")).getText();
//        List<WebElement> elements = webElement.findElements(By.cssSelector("div.info span"));

//        String createdAt;
//        String publicationTime;
//
//        for (int i = 0; i < elements.size(); i++) {
//            if (i == 0) {
//                createdAt = elements.get(i).getText();
//            } else if (i == 1) {
//                publicationTime = elements.get(i).getText();
//            }
//        }

        String createdAt = webElement.findElement(By.cssSelector("div.info span:nth-child(1)")).getText();
        String publicationTime = webElement.findElement(By.cssSelector("div.info span:nth-child(2)")).getText();
        String originArticleUrl = webElement.findElement(By.cssSelector("div.info a")).getAttribute("href");
//        webElement.findElements(By.cssSelector(""))

        Thread.sleep(50);
        driver.quit();

        return HeaderDto.of(outerHtml, articleTitle, createdAt, publicationTime, originArticleUrl, null);
    }
}
