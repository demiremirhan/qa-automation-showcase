package io.github.demiremirhan.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GridConnectionTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() throws MalformedURLException {
        String gridUrl = System.getProperty("selenium.grid.url", "http://localhost:4444/wd/hub");
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL(gridUrl), options);
    }

    @Test
    void canConnectToGridAndLoadPage() {
        driver.get("https://www.google.com");
        assertTrue(driver.getTitle().contains("Google"));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}