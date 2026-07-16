package io.github.demiremirhan.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private static final By CART_ITEMS      = By.className("cart_item");
    private static final By ITEM_NAME       = By.className("inventory_item_name");
    private static final By CHECKOUT_BUTTON = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        return findAll(CART_ITEMS).size();
    }

    public List<String> getItemNames() {
        return findAll(ITEM_NAME).stream()
                .map(el -> el.getText())
                .collect(Collectors.toList());
    }

    @Step("Remove '{name}' from cart")
    public CartPage removeItem(String name) {
        String buttonId = "remove-" + name.toLowerCase().replace(" ", "-");
        click(By.id(buttonId));
        return this;
    }

    @Step("Proceed to checkout")
    public CheckoutPage checkout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutPage(driver);
    }
}