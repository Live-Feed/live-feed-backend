package com.livefeed.livefeedbatch.urlcrawler.reader;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

class NaverNewsItemReaderTest {

    @DisplayName("headless로 브라우저 크롤링하는 테스트")
    @Test
    void headless() throws InterruptedException {
        // given
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        // when
        driver.get("https://naver.com");
        // then
        Thread.sleep(1000);
        driver.quit();
    }
}