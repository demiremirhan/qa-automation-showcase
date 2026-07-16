package io.github.demiremirhan.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    // Step One
    private static final By FIRST_NAME_INPUT = By.id("first-name");
    private static final By LAST_NAME_INPUT  = By.id("last-name");
    private static final By POSTAL_INPUT     = By.id("postal-code");
    private static final By CONTINUE_BUTTON  = By.id("continue");

    // Step Two (overview)
    private static final By SUMMARY_TOTAL    = By.className("summary_total_label");
    private static final By FINISH_BUTTON    = By.id("finish");

    // Complete
    private static final By COMPLETE_HEADER  = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Step("Fill checkout info: {firstName} {lastName}, {postalCode}")
    public CheckoutPage fillInfo(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME_INPUT, firstName);
        type(LAST_NAME_INPUT, lastName);
        type(POSTAL_INPUT, postalCode);
        click(CONTINUE_BUTTON);
        return this;
    }

    public String getTotalLabel() {
        return getText(SUMMARY_TOTAL);
    }

    @Step("Finish order")
    public CheckoutPage finish() {
        click(FINISH_BUTTON);
        return this;
    }

    public String getCompleteHeader() {
        return getText(COMPLETE_HEADER);
    }
}