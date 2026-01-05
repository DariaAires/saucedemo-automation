package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductsPage extends BasePage {
    
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;
    
    @FindBy(id = "inventory_container")
    private WebElement inventoryContainer;
    
    public ProductsPage(WebDriver driver) {
        super(driver);
    }
    
    public String getPageTitle() {
        String title = pageTitle.getText();
        logger.info("Заголовок страницы: {}", title);
        return title;
    }
    
    public boolean isProductsPageDisplayed() {
        boolean displayed = inventoryContainer.isDisplayed() && 
                          pageTitle.isDisplayed() && 
                          pageTitle.getText().equals("Products");
        logger.debug("Страница продуктов отображается: {}", displayed);
        return displayed;
    }
    
    public boolean isMenuDisplayed() {
        boolean displayed = menuButton.isDisplayed();
        logger.debug("Меню отображается: {}", displayed);
        return displayed;
    }
}
