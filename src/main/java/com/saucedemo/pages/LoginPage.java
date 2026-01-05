package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    
    @FindBy(id = "user-name")
    private WebElement usernameInput;
    
    @FindBy(id = "password")
    private WebElement passwordInput;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    public void open(String url) {
        driver.get(url);
        logger.info("Открыта страница: {}", url);
    }
    
    public void enterUsername(String username) {
        usernameInput.clear();
        usernameInput.sendKeys(username);
        logger.info("Введен username: {}", username);
    }
    
    public void enterPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
        logger.info("Введен пароль");
    }
    
    public void clickLoginButton() {
        loginButton.click();
        logger.info("Нажата кнопка логина");
    }
    
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        logger.info("Выполнен логин для пользователя: {}", username);
    }
    
    public String getErrorMessage() {
        String error = errorMessage.getText();
        logger.info("Получено сообщение об ошибке: {}", error);
        return error;
    }
    
    public boolean isErrorMessageDisplayed() {
        boolean displayed = errorMessage.isDisplayed();
        logger.debug("Сообщение об ошибке отображается: {}", displayed);
        return displayed;
    }
}
