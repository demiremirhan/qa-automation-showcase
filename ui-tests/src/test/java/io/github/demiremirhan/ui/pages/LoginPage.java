package io.github.demiremirhan.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON   = By.id("login-button");
    private static final By ERROR_MESSAGE  = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open login page")
    public LoginPage open(String baseUrl) {
        driver.get(baseUrl);
        return this;
    }

    @Step("Login as {username}")
    public ProductsPage loginAs(String username, String password) {
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
        return new ProductsPage(driver);
    }

    @Step("Attempt login as {username} (expecting failure)")
    public LoginPage loginExpectingError(String username, String password) {
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
        return this;
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}