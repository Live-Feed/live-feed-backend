package com.livefeed.livefeedcrawler.batch.reader;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@StepScope
@Component
public class GoogleNewsItemReader extends AbstractItemCountingItemStreamItemReader<String> {

    @Value("#{jobParameters[pageUrl]}")
    private String pageUrl;

    private final List<String> articleUrls = new ArrayList<>();

    public GoogleNewsItemReader() {
        super();
        setExecutionContextName(ClassUtils.getShortName(GoogleNewsItemReader.class));
    }

    @Override
    protected String doRead() {
        if (articleUrls.isEmpty()) {
            return null;
        }

        return articleUrls.remove(0);
    }

    @Override
    protected void doOpen() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

        try {
            driver.get(pageUrl);

            while (true) {
                double currentScrollPosition = getCurrentScrollPosition(javascriptExecutor);
                scrollDown(javascriptExecutor);
                if (hasNoMoreArticles(javascriptExecutor, currentScrollPosition)) {
                    break;
                }

                Thread.sleep(3000);
                readArticles(driver);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            driver.quit();
        }
    }

    @Override
    protected void doClose() {
        articleUrls.clear();
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

    private void readArticles(WebDriver driver) {
        List<WebElement> articleLinks = driver.findElements(By.cssSelector("[jsname='hXwDdf']"));

        for (WebElement linkElement : articleLinks) {
            String articleUrl = linkElement.getAttribute("href");
            articleUrls.add(articleUrl);
        }
    }
}
