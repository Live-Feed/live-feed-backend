package com.livefeed.livefeedbatch.batch.reader;

import com.livefeed.livefeedbatch.batch.common.driver.ChromeDriverProvider;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@StepScope
@Component
public class NaverNewsUrlReader extends AbstractPaginatedDataItemReader<String> {

    @Value("#{jobParameters[pageUrl]}")
    private String pageUrl;

    @Value("#{jobParameters[date]}")
    private LocalDateTime date;

    private int maxPage;
    private static final int PAGE_SIZE = 20;
    private static final int PAGINATION_SIZE = 10;
    private static final int MAX_PUBLICATION_TIME_DIFFERENCE_IN_MINUTES = 30;
    private static final DateTimeFormatter searchDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter publicationTimeFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    public NaverNewsUrlReader() {
        super();
        setPageSize(PAGE_SIZE);
        setExecutionContextName(ClassUtils.getShortName(NaverNewsUrlReader.class));
    }

    @Override
    protected Iterator<String> doPageRead() {
        if (page > maxPage) {
            return null;
        }

        WebDriver driver = ChromeDriverProvider.getDriver();
        List<String> articleUrls = new ArrayList<>();

        try {
            openPage(driver, page);
            readArticles(driver, articleUrls);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            driver.quit();
        }
        return articleUrls.iterator();
    }

    @Override
    protected void doOpen() {
        WebDriver driver = ChromeDriverProvider.getDriver();

        try {
            setMaxPage(driver);
            log.info("setMaxPage: {}", maxPage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            driver.quit();
        }
    }

    @Override
    protected void doClose() {
        complete();
    }

    private void openPage(WebDriver driver, int page) {
        String queryParameter = "?date=" + searchDateFormat.format(date) + "&isphoto=N&page=" + (page + 1);
        driver.get(pageUrl + queryParameter);
    }

    private void setMaxPage(WebDriver driver) {
        openPage(driver, maxPage);

        WebElement paginateElement = driver.findElement(By.cssSelector(".paginate"));
        boolean nextButtonExists = paginateElement.findElements(By.linkText("다음")).size() > 0;

        if (nextButtonExists) {
            maxPage += PAGINATION_SIZE;
            setMaxPage(driver);
        } else {
            List<WebElement> dataIdElements = paginateElement.findElements(By.cssSelector("a[data-id]"));
            for (WebElement dataIdElement : dataIdElements) {
                String dataIdValue = dataIdElement.getAttribute("data-id");
                int pageValue = Integer.parseInt(dataIdValue) - 1;
                if (pageValue > maxPage) {
                    maxPage = pageValue;
                }
            }
        }
    }

    private void readArticles(WebDriver driver, List<String> articleUrls) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".news_list .text")));
        } catch (TimeoutException e) {
            log.info("The article has not been uploaded yet.");
            return;
        }

        List<WebElement> articleList = driver.findElements(By.cssSelector(".news_list .text"));

        for (WebElement articleElement : articleList) {
            if (isArticleOverTimeLimit(articleElement)) {
                complete();
                break;
            }

            String articleUrl = articleElement.findElement(By.cssSelector(".title")).getAttribute("href");
            articleUrls.add(articleUrl);
        }
    }

    private boolean isArticleOverTimeLimit(WebElement articleElement) {
        String publicationTime = articleElement.findElement(By.cssSelector(".time")).getText();

        try {
            LocalDateTime publicationDate = LocalDateTime.parse(publicationTime, publicationTimeFormat);
            Duration timeDifference = Duration.between(publicationDate, date);
            if (timeDifference.toMinutes() > MAX_PUBLICATION_TIME_DIFFERENCE_IN_MINUTES) {
                return true;
            }
        } catch (Exception e) {
            log.error(e. getMessage(), e);
        }

        return false;
    }

    private void complete() {
        maxPage = 0;
    }
}
