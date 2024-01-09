package com.livefeed.livefeedbatch.urlcrawler.reader;

import com.livefeed.livefeedbatch.batch.common.driver.FirefoxDriverProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

class NaverNewsUrlReaderTest {

    @DisplayName("headless로 브라우저 크롤링하는 테스트")
    @Test
    void headless() throws InterruptedException {
        // given
        WebDriver driver = FirefoxDriverProvider.getDriver();
        // when
        driver.get("https://naver.com");
        // then
        Thread.sleep(1000);
        driver.quit();
    }
}