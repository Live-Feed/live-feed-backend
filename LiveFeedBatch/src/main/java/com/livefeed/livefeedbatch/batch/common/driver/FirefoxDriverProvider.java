package com.livefeed.livefeedbatch.batch.common.driver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

@Slf4j
public class FirefoxDriverProvider {

    private static final String driverPath = getDriverPath();
    private static final FirefoxOptions firefoxOptions = setFirefoxOptions();

    public static WebDriver getDriver() {
        System.setProperty("webdriver.gecko.driver", driverPath);
        System.setProperty("webdriver.firefox.port", "16000");
        return new FirefoxDriver(firefoxOptions);
    }

    private static String getDriverPath() {
        // mac / docker container(linux) 환경 구분
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac")) {
            return "/opt/homebrew/bin/geckodriver";
        } else {
            return "/usr/bin/geckodriver";
        }
    }

    private static FirefoxOptions setFirefoxOptions() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("layers.acceleration.disabled", true);

        return new FirefoxOptions()
                .addArguments("--headless")
                .setProfile(profile);
    }
}
