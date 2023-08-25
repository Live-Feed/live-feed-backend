package com.livefeed.livefeedcrawler.batch;

import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@StepScope
@NonNullApi
@RequiredArgsConstructor
public class GoogleNewsCrawlTasklet implements Tasklet {

    @Value("#{jobParameters[pageUrl]}")
    private String pageUrl;

    private final KafkaProducerTemplate<String, String> kafkaProducer;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
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
                sendArticleUrls(driver);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            driver.quit();
        }

        return RepeatStatus.FINISHED;
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
            kafkaProducer.sendMessage("LIVEFEED.STREAM.ARTICLE.URL", articleUrl);
        }
    }
}
