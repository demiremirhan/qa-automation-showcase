package io.github.demiremirhan.ui.tests;

import io.github.demiremirhan.common.config.ConfigProvider;
import io.github.demiremirhan.ui.driver.DriverFactory;
import io.github.demiremirhan.ui.pages.LoginPage;
import io.github.demiremirhan.ui.pages.ProductsPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public abstract class BaseTest {

    protected WebDriver driver;
    protected String baseUrl;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.get();
        baseUrl = ConfigProvider.get().uiBaseUrl();
    }

    @AfterEach
    void tearDown() {
        DriverFactory.quit();
    }

    protected ProductsPage loginAsStandardUser() {
        return new LoginPage(driver)
                .open(baseUrl)
                .loginAs("standard_user", "secret_sauce");
    }
}