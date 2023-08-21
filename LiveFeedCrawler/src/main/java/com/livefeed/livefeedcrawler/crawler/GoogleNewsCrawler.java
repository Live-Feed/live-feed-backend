package com.livefeed.livefeedcrawler.crawler;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleNewsCrawler implements NewsCrawlerTemplate {

    public void crawlPage() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

        String pageUrl = "https://news.google.com/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRFp1ZEdvU0FtdHZHZ0pMVWlnQVAB?hl=ko&gl=KR&ceid=KR%3Ako";
        driver.get(pageUrl);

        try {
            while (true) {
                double currentScrollPosition = getCurrentScrollPosition(javascriptExecutor);
                scrollDown(javascriptExecutor);
                if (hasNoMoreArticles(javascriptExecutor, currentScrollPosition)) {
                    break;
                }

                Thread.sleep(3000);
                sendArticleUrls(driver);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            driver.quit();
        }
    }

    private void scrollDown(JavascriptExecutor javascriptExecutor) {
        javascriptExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    private double getCurrentScrollPosition(JavascriptExecutor javascriptExecutor) {
        return Optional.ofNullable(javascriptExecutor.executeScript("return window.scrollY;"))
                .filter(value -> value instanceof Number)
                .map(value -> ((Number) value).doubleValue())
                .orElse(0.0);
    }

    private boolean hasNoMoreArticles(JavascriptExecutor javascriptExecutor, double lastScrollPosition) {
        return getCurrentScrollPosition(javascriptExecutor) == lastScrollPosition;
    }

    private void sendArticleUrls(WebDriver driver) {
        List<WebElement> articleLinks = driver.findElements(By.cssSelector("[jsname='hXwDdf']"));

        for (WebElement linkElement : articleLinks) {
            String articleUrl = linkElement.getAttribute("href");
            // TODO: send to kafka
        }
    }

}
