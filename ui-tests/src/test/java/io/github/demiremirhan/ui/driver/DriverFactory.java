package io.github.demiremirhan.ui.driver;

import io.github.demiremirhan.common.config.ConfigProvider;
import io.github.demiremirhan.common.config.FrameworkConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver get() {
        if (DRIVER.get() == null) {
            DRIVER.set(createDriver());
        }
        return DRIVER.get();
    }

    public static void quit() {
        WebDriver d = DRIVER.get();
        if (d != null) {
            d.quit();
            DRIVER.remove();
        }
    }

    private static WebDriver createDriver() {
        FrameworkConfig cfg = ConfigProvider.get();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox",
                "--disable-dev-shm-usage", "--window-size=1920,1080");

        try {
            WebDriver driver = new RemoteWebDriver(new URL(cfg.seleniumGridUrl()), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Grid URL: " + cfg.seleniumGridUrl(), e);
        }
    }
}