package com.saucedemo.utils;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class TestListener implements TestWatcher {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    
    @Override
    public void testSuccessful(ExtensionContext context) {
        String testName = context.getDisplayName();
        logger.info("✅ Тест успешен: {}", testName);
        DriverManager.quitDriver();
    }
    
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        logger.error("❌ Тест провален: {} - {}", testName, cause.getMessage());
        
        // Скриншот при падении
        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Скриншот при падении", 
                new ByteArrayInputStream(screenshot));
        } catch (Exception e) {
            logger.error("Не удалось сделать скриншот: {}", e.getMessage());
        }
        
        DriverManager.quitDriver();
    }
    
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        logger.warn("⚠️ Тест прерван: {}", context.getDisplayName());
        DriverManager.quitDriver();
    }
    
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        logger.warn("🚫 Тест отключен: {}", context.getDisplayName());
    }
}
