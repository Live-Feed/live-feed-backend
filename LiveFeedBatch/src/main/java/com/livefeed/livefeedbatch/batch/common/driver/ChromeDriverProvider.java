package com.livefeed.livefeedbatch.batch.common.driver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

@Slf4j
public class ChromeDriverProvider {

    private static final String driverPath = getDriverPath();
    private static final ChromeOptions chromeOptions = setChromeOptions();

    public static WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", driverPath);
//        System.setProperty("webdriver.chrome.whitelistedIps", "");
        ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(driverPath))
                .usingPort(16000)
                .build();

        try {
            chromeDriverService.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return new ChromeDriver(chromeDriverService, chromeOptions);
    }

    private static String getDriverPath() {
        // mac / docker container(linux) 환경 구분
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac")) {
            return "/opt/homebrew/bin/chromedriver";
        } else {
            return "/app/usr/bin/chromedriver";
//            return "/usr/local/bin";
        }
    }

    private static ChromeOptions setChromeOptions() {
        return new ChromeOptions()
                .addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage", "--disable-gpu", "--disable-extensions",
                        "--incognito", "--disable-setuid-sandbox", "--disable-infobars", "--single-process", "--remote-debugging-port=9222");
    }
}
