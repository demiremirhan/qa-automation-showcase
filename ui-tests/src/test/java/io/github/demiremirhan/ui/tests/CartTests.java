package io.github.demiremirhan.ui.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Saucedemo E2E")
@Feature("Shopping Cart")
class CartTests extends BaseTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Add single item to cart and verify badge count")
    void addSingleItemToCart() {
        var productsPage = loginAsStandardUser();
        productsPage.addItemToCartByName("sauce-labs-backpack");

        assertEquals(1, productsPage.getCartBadgeCount());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Add multiple items and verify cart contents")
    void addMultipleItemsAndVerifyCart() {
        var productsPage = loginAsStandardUser();
        productsPage
                .addItemToCartByName("sauce-labs-backpack")
                .addItemToCartByName("sauce-labs-bike-light");

        assertEquals(2, productsPage.getCartBadgeCount());

        var cartPage = productsPage.goToCart();
        assertEquals(2, cartPage.getItemCount());

        var names = cartPage.getItemNames();
        assertTrue(names.contains("Sauce Labs Backpack"));
        assertTrue(names.contains("Sauce Labs Bike Light"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Remove item from cart")
    void removeItemFromCart() {
        var cartPage = loginAsStandardUser()
                .addItemToCartByName("sauce-labs-backpack")
                .addItemToCartByName("sauce-labs-bike-light")
                .goToCart();

        cartPage.removeItem("sauce-labs-backpack");
        assertEquals(1, cartPage.getItemCount());
    }
}