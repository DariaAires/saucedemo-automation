package com.saucedemo.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для управления веб-драйвером (Singleton)
 * Поддерживает Chrome, Firefox, Edge, Safari
 * Автоматически скачивает драйверы через WebDriverManager
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static DriverManager instance;
    private WebDriver driver;
    private ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    
    // Приватный конструктор для Singleton
    private DriverManager() {
        // Конструктор оставляем пустым, драйвер инициализируется при первом вызове getDriver()
    }
    
    /**
     * Получение экземпляра DriverManager (Singleton)
     */
    public static synchronized DriverManager getInstance() {
        if (instance == null) {
            instance = new DriverManager();
            logger.debug("Создан новый экземпляр DriverManager");
        }
        return instance;
    }
    
    /**
     * Получение веб-драйвера (ленивая инициализация)
     */
    public WebDriver getDriver() {
        if (threadLocalDriver.get() == null) {
            logger.info("Инициализация драйвера для потока: {}", Thread.currentThread().getId());
            initializeDriver();
        }
        return threadLocalDriver.get();
    }
    
    /**
     * Инициализация драйвера на основе конфигурации
     */
    private void initializeDriver() {
        ConfigReader config = ConfigReader.getInstance();
        String browser = config.getBrowser().toLowerCase();
        
        logger.info("Создание драйвера для браузера: {}", browser);
        
        switch (browser) {
            case "chrome":
                setupChromeDriver();
                break;
            case "firefox":
                setupFirefoxDriver();
                break;
            case "edge":
                setupEdgeDriver();
                break;
            case "safari":
                setupSafariDriver();
                break;
            default:
                logger.error("Неподдерживаемый браузер: {}", browser);
                throw new IllegalArgumentException("Неподдерживаемый браузер: " + browser);
        }
        
        setupDriverTimeouts();
        maximizeWindow();
        
        logger.info("Драйвер успешно инициализирован для браузера: {}", browser);
    }
    
    /**
     * Настройка Chrome драйвера
     */
    private void setupChromeDriver() {
        logger.debug("Настройка Chrome драйвера");
        
        // Автоматическое скачивание и настройка ChromeDriver
        WebDriverManager.getInstance(DriverManagerType.CHROME).setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Добавляем аргументы для улучшения стабильности
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        // Отключаем сохранение паролей
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        
        driver = new ChromeDriver(options);
        threadLocalDriver.set(driver);
    }
    
    /**
     * Настройка Firefox драйвера
     */
    private void setupFirefoxDriver() {
        logger.debug("Настройка Firefox драйвера");
        
        WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
        
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        
        driver = new FirefoxDriver(options);
        threadLocalDriver.set(driver);
    }
    
    /**
     * Настройка Edge драйвера
     */
    private void setupEdgeDriver() {
        logger.debug("Настройка Edge драйвера");
        
        WebDriverManager.getInstance(DriverManagerType.EDGE).setup();
        
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        
        driver = new EdgeDriver(options);
        threadLocalDriver.set(driver);
    }
    
    /**
     * Настройка Safari драйвера
     */
    private void setupSafariDriver() {
        logger.debug("Настройка Safari драйвера");
        
        // Для Safari не нужен WebDriverManager
        driver = new SafariDriver();
        threadLocalDriver.set(driver);
    }
    
    /**
     * Настройка таймаутов драйвера
     */
    private void setupDriverTimeouts() {
        ConfigReader config = ConfigReader.getInstance();
        
        driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(config.getImplicitWait())
        );
        
        driver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(config.getPageLoadTimeout())
        );
        
        driver.manage().timeouts().scriptTimeout(
            Duration.ofSeconds(30)
        );
        
        logger.debug("Таймауты драйвера установлены: implicit={}s, pageLoad={}s", 
            config.getImplicitWait(), config.getPageLoadTimeout());
    }
    
    /**
     * Максимизация окна браузера
     */
    private void maximizeWindow() {
        try {
            driver.manage().window().maximize();
            logger.debug("Окно браузера максимизировано");
        } catch (Exception e) {
            logger.warn("Не удалось максимизировать окно браузера: {}", e.getMessage());
        }
    }
    
    /**
     * Закрытие драйвера
     */
    public void quitDriver() {
        if (threadLocalDriver.get() != null) {
            try {
                logger.info("Закрытие драйвера для потока: {}", Thread.currentThread().getId());
                threadLocalDriver.get().quit();
                logger.debug("Драйвер успешно закрыт");
            } catch (Exception e) {
                logger.error("Ошибка при закрытии драйвера: {}", e.getMessage());
            } finally {
                threadLocalDriver.remove();
            }
        }
    }
    
    /**
     * Удаление всех драйверов (для завершения работы)
     */
    public static void quitAllDrivers() {
        if (instance != null) {
            instance.quitDriver();
            instance = null;
            logger.info("Все драйверы закрыты");
        }
    }
}