package io.github.demiremirhan.ui.tests;

import io.github.demiremirhan.ui.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Saucedemo E2E")
@Feature("Login")
class LoginTests extends BaseTest {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Standard user can login successfully")
    void standardUserCanLogin() {
        var productsPage = loginAsStandardUser();
        assertEquals("Products", productsPage.getTitle());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Locked-out user sees error message")
    void lockedOutUserSeesError() {
        var loginPage = new LoginPage(driver)
                .open(baseUrl)
                .loginExpectingError("locked_out_user", "secret_sauce");

        assertTrue(loginPage.getErrorMessage().contains("locked out"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Invalid credentials show error")
    void invalidCredentialsShowError() {
        var loginPage = new LoginPage(driver)
                .open(baseUrl)
                .loginExpectingError("invalid_user", "wrong_password");

        assertTrue(loginPage.getErrorMessage().contains("do not match"));
    }
}