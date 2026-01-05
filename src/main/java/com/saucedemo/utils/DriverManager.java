package com.saucedemo.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static WebDriver driver;
    private static DriverManager instance;
    
    private DriverManager() {
        // Приватный конструктор
    }
    
    public static synchronized DriverManager getInstance() {
        if (instance == null) {
            instance = new DriverManager();
        }
        return instance;
    }
    
    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }
    
    private static void initializeDriver() {
        logger.info("Инициализация Chrome драйвера");
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        
        driver = new ChromeDriver(options);
        
        // Настройка таймаутов
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        logger.info("Драйвер успешно инициализирован");
    }
    
    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Драйвер закрыт");
            } catch (Exception e) {
                logger.error("Ошибка при закрытии драйвера: {}", e.getMessage());
            } finally {
                driver = null;
                instance = null;
            }
        }
    }
    
    public static void quitAllDrivers() {
        quitDriver();
    }
}
