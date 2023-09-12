package com.livefeed.livefeedcrawler.batch.reader;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@StepScope
@Component
public class NaverNewsItemReader extends AbstractPaginatedDataItemReader<String> {

    @Value("#{jobParameters[pageUrl]}")
    private String pageUrl;

    @Value("#{jobParameters[date]}")
    private Date date;

    private int maxPage;
    private static final int PAGE_SIZE = 20;
    private static final int PAGINATION_SIZE = 10;
    private static final int MAX_PUBLICATION_TIME_DIFFERENCE_IN_MINUTES = 30;
    private static final SimpleDateFormat searchDateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat publicationTimeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    public NaverNewsItemReader() {
        super();
        setPageSize(PAGE_SIZE);
        setExecutionContextName(ClassUtils.getShortName(NaverNewsItemReader.class));
    }

    @Override
    protected Iterator<String> doPageRead() {
        if (page > maxPage) {
            return null;
        }

        WebDriver driver = new ChromeDriver();
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
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            setMaxPage(driver);
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
            Date publicationDate = publicationTimeFormat.parse(publicationTime);
            long timeDifference = (date.getTime() - publicationDate.getTime()) / (1000 * 60);
            if (timeDifference > MAX_PUBLICATION_TIME_DIFFERENCE_IN_MINUTES) {
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
