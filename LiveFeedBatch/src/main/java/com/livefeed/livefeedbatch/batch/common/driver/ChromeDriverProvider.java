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
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromium-driver");
        ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(driverPath))
                .usingPort(16000)
                .build();

        try {
            chromeDriverService.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        chromeOptions.setBinary("/usr/bin/chromium-browser");
        return new ChromeDriver(chromeDriverService, chromeOptions);
    }

    private static String getDriverPath() {
        // mac / docker container(linux) 환경 구분
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac")) {
            return "/opt/homebrew/bin/chromedriver";
        } else {
            return "/usr/bin/chromium-driver";
        }
    }

    private static ChromeOptions setChromeOptions() {
        return new ChromeOptions()
                .addArguments("headless");
    }
}
