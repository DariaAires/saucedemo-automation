package com.saucedemo.constants;

import com.saucedemo.utils.ConfigReader;

/**
 * Класс для хранения всех констант проекта
 * Использует ConfigReader для получения значений из конфигурации
 */
public class Constants {
    
    // Конфигурация приложения
    public static final String BASE_URL = ConfigReader.getInstance().getBaseUrl();
    public static final String BROWSER = ConfigReader.getInstance().getBrowser();
    
    // Учетные данные пользователей
    public static final String STANDARD_USER = ConfigReader.getInstance().getStandardUser();
    public static final String STANDARD_PASSWORD = ConfigReader.getInstance().getStandardPassword();
    public static final String LOCKED_USER = ConfigReader.getInstance().getLockedUser();
    public static final String PERFORMANCE_USER = ConfigReader.getInstance().getPerformanceUser();
    
    // Тестовые данные для негативных сценариев
    public static final String INVALID_USER = "invalid_user";
    public static final String INVALID_PASSWORD = "invalid_password";
    public static final String EMPTY_STRING = "";
    
    // Сообщения об ошибках (ожидаемые тексты ошибок с сайта)
    public static final String ERROR_EMPTY_USERNAME = "Epic sadface: Username is required";
    public static final String ERROR_EMPTY_PASSWORD = "Epic sadface: Password is required";
    public static final String ERROR_INVALID_CREDENTIALS = 
        "Epic sadface: Username and password do not match any user in this service";
    public static final String ERROR_LOCKED_USER = 
        "Epic sadface: Sorry, this user has been locked out.";
    
    // Селекторы элементов (если используются часто)
    public static final String USERNAME_INPUT_ID = "user-name";
    public static final String PASSWORD_INPUT_ID = "password";
    public static final String LOGIN_BUTTON_ID = "login-button";
    public static final String ERROR_MESSAGE_CSS = "[data-test='error']";
    public static final String LOGIN_LOGO_CLASS = "login_logo";
    public static final String PRODUCTS_TITLE_CLASS = "title";
    public static final String MENU_BUTTON_ID = "react-burger-menu-btn";
    public static final String INVENTORY_CONTAINER_ID = "inventory_container";
    
    // Таймауты
    public static final int IMPLICIT_WAIT = ConfigReader.getInstance().getImplicitWait();
    public static final int PAGE_LOAD_TIMEOUT = ConfigReader.getInstance().getPageLoadTimeout();
    public static final int EXPLICIT_WAIT = 10;
    
    // Настройки отчетов
    public static final boolean SCREENSHOT_ON_FAILURE = 
        ConfigReader.getInstance().isScreenshotOnFailure();
    public static final String SCREENSHOT_DIRECTORY = 
        ConfigReader.getInstance().getScreenshotDirectory();
    
    // Тестовые граничные значения
    public static final long PERFORMANCE_THRESHOLD_MS = 10000; // 10 секунд
    public static final int MAX_LOGIN_ATTEMPTS = 3;
    
    // URL endpoints
    public static final String LOGIN_PAGE_URL = BASE_URL;
    public static final String PRODUCTS_PAGE_URL = BASE_URL + "inventory.html";
    public static final String CART_PAGE_URL = BASE_URL + "cart.html";
    
    // Названия страниц (для логирования и отчетов)
    public static final String LOGIN_PAGE_NAME = "Login Page";
    public static final String PRODUCTS_PAGE_NAME = "Products Page";
    public static final String CART_PAGE_NAME = "Cart Page";
}