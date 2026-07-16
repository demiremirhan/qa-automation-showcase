package io.github.demiremirhan.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPage extends BasePage {

    private static final By PAGE_TITLE      = By.className("title");
    private static final By INVENTORY_ITEMS = By.className("inventory_item");
    private static final By CART_BADGE      = By.className("shopping_cart_badge");
    private static final By CART_LINK       = By.className("shopping_cart_link");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return getText(PAGE_TITLE);
    }

    public int getItemCount() {
        return findAll(INVENTORY_ITEMS).size();
    }

    @Step("Add '{name}' to cart")
    public ProductsPage addItemToCartByName(String name) {
        String buttonId = "add-to-cart-" + name.toLowerCase().replace(" ", "-");
        click(By.id(buttonId));
        return this;
    }

    public int getCartBadgeCount() {
        try {
            return Integer.parseInt(getText(CART_BADGE));
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Go to cart")
    public CartPage goToCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }
}