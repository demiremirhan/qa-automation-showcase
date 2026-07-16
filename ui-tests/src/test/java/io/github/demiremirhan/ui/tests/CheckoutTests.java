package io.github.demiremirhan.ui.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Saucedemo E2E")
@Feature("Checkout")
class CheckoutTests extends BaseTest {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Complete checkout for single item")
    void completeSingleItemCheckout() {
        var checkoutPage = loginAsStandardUser()
                .addItemToCartByName("sauce-labs-backpack")
                .goToCart()
                .checkout();

        checkoutPage.fillInfo("Emirhan", "Demir", "42103");

        String total = checkoutPage.getTotalLabel();
        assertTrue(total.contains("$"));

        checkoutPage.finish();
        assertEquals("Thank you for your order!", checkoutPage.getCompleteHeader());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Complete checkout with multiple items")
    void completeMultiItemCheckout() {
        var checkoutPage = loginAsStandardUser()
                .addItemToCartByName("sauce-labs-backpack")
                .addItemToCartByName("sauce-labs-bolt-t-shirt")
                .goToCart()
                .checkout();

        checkoutPage.fillInfo("Test", "User", "10001");
        checkoutPage.finish();

        assertEquals("Thank you for your order!", checkoutPage.getCompleteHeader());
    }
}