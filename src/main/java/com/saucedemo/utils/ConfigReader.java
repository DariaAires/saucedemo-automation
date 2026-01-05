package com.saucedemo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для чтения конфигурационных параметров из файла config.properties
 * Использует паттерн Singleton для гарантии единственного экземпляра
 */
public class ConfigReader {
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();
    private static ConfigReader instance;
    
    // Приватный конструктор для Singleton
    private ConfigReader() {
        loadProperties();
    }
    
    /**
     * Метод для получения экземпляра ConfigReader (Singleton)
     */
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }
    
    /**
     * Загрузка свойств из файла config.properties
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            
            if (input == null) {
                logger.error("Файл config.properties не найден в classpath");
                throw new RuntimeException("Файл config.properties не найден");
            }
            
            properties.load(input);
            logger.info("Конфигурационные параметры успешно загружены");
            
        } catch (IOException e) {
            logger.error("Ошибка при загрузке файла config.properties: {}", e.getMessage());
            throw new RuntimeException("Ошибка загрузки конфигурации", e);
        }
    }
    
    /**
     * Получение значения свойства по ключу
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Свойство {} не найдено в конфигурации", key);
        }
        return value;
    }
    
    /**
     * Получение значения свойства с дефолтным значением
     */
    public String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        logger.debug("Свойство {} = {}", key, value);
        return value;
    }
    
    /**
     * Получение целочисленного свойства
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Неверный формат числа для свойства {}: {}", key, value);
            throw new RuntimeException("Неверный формат конфигурационного параметра", e);
        }
    }
    
    /**
     * Получение логического свойства
     */
    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }
    
    // Геттеры для конкретных свойств (удобство использования)
    
    public String getBaseUrl() {
        return getProperty("base.url");
    }
    
    public String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    public String getStandardUser() {
        return getProperty("standard.user");
    }
    
    public String getStandardPassword() {
        return getProperty("standard.password");
    }
    
    public String getLockedUser() {
        return getProperty("locked.user");
    }
    
    public String getPerformanceUser() {
        return getProperty("performance.user");
    }
    
    public int getImplicitWait() {
        return getIntProperty("implicit.wait");
    }
    
    public int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout");
    }
    
    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure");
    }
    
    public String getScreenshotDirectory() {
        return getProperty("screenshot.directory");
    }
}