package com.saucedemo.utils;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * JUnit 5 Extension для логирования, скриншотов и Allure отчетов
 * Реализует несколько callback интерфейсов для полного контроля над жизненным циклом теста
 */
public class TestListener implements BeforeAllCallback, BeforeEachCallback, 
                                     AfterEachCallback, AfterAllCallback, TestWatcher {
    
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    
    /**
     * Выполняется перед всеми тестами в классе
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        String className = context.getTestClass()
            .map(Class::getSimpleName)
            .orElse("UnknownClass");
        
        logger.info("╔══════════════════════════════════════════════════════════════╗");
        logger.info("║                 Начало выполнения тестового класса           ║");
        logger.info("║                    Класс: {}                           ║", 
            String.format("%-35s", className));
        logger.info("╚══════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Выполняется перед каждым тестом
     */
    @Override
    public void beforeEach(ExtensionContext context) {
        String testName = context.getDisplayName();
        String methodName = context.getTestMethod()
            .map(Method::getName)
            .orElse("UnknownMethod");
        
        logger.info("────────────────────────────────────────────────────────────────");
        logger.info("▶ Начало теста: {}", testName);
        logger.info("  Метод: {}", methodName);
        logger.info("  Поток: {}", Thread.currentThread().getId());
        logger.info("────────────────────────────────────────────────────────────────");
        
        // Добавляем информацию о тесте в Allure
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName(testName);
        });
    }
    
    /**
     * Выполняется после успешного теста
     */
    @Override
    public void testSuccessful(ExtensionContext context) {
        String testName = context.getDisplayName();
        
        logger.info("────────────────────────────────────────────────────────────────");
        logger.info("✓ Тест УСПЕШЕН: {}", testName);
        logger.info("────────────────────────────────────────────────────────────────");
        
        // Закрываем драйвер после успешного теста
        DriverManager.getInstance().quitDriver();
    }
    
    /**
     * Выполняется после неудачного теста
     */
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        
        logger.error("────────────────────────────────────────────────────────────────");
        logger.error("✗ Тест ПРОВАЛЕН: {}", testName);
        logger.error("  Причина: {}", cause.getMessage());
        logger.error("────────────────────────────────────────────────────────────────");
        
        // Делаем скриншот при падении теста
        takeScreenshotOnFailure(testName);
        
        // Добавляем информацию об ошибке в Allure
        Allure.addAttachment("Ошибка теста", 
            "text/plain", 
            cause.getMessage(), 
            ".txt");
        
        // Закрываем драйвер после неудачного теста
        DriverManager.getInstance().quitDriver();
    }
    
    /**
     * Выполняется после прерванного теста
     */
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        
        logger.warn("────────────────────────────────────────────────────────────────");
        logger.warn("⚠ Тест ПРЕРВАН: {}", testName);
        logger.warn("  Причина: {}", cause.getMessage());
        logger.warn("────────────────────────────────────────────────────────────────");
        
        DriverManager.getInstance().quitDriver();
    }
    
    /**
     * Выполняется после пропущенного теста
     */
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String testName = context.getDisplayName();
        String reasonText = reason.orElse("Не указана");
        
        logger.warn("────────────────────────────────────────────────────────────────");
        logger.warn("⊘ Тест ПРОПУЩЕН: {}", testName);
        logger.warn("  Причина: {}", reasonText);
        logger.warn("────────────────────────────────────────────────────────────────");
    }
    
    /**
     * Выполняется после каждого теста (вне зависимости от результата)
     */
    @Override
    public void afterEach(ExtensionContext context) {
        // Можно добавить дополнительную логику очистки
    }
    
    /**
     * Выполняется после всех тестов в классе
     */
    @Override
    public void afterAll(ExtensionContext context) {
        String className = context.getTestClass()
            .map(Class::getSimpleName)
            .orElse("UnknownClass");
        
        logger.info("════════════════════════════════════════════════════════════════");
        logger.info(" Завершение выполнения тестового класса: {}", className);
        logger.info("════════════════════════════════════════════════════════════════");
        
        // Гарантируем закрытие всех драйверов
        DriverManager.quitAllDrivers();
    }
    
    /**
     * Создание скриншота при падении теста
     */
    private void takeScreenshotOnFailure(String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getInstance().getDriver();
            if (ts != null) {
                byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                
                // Добавляем скриншот в Allure отчет
                Allure.addAttachment(
                    "Скриншот при падении: " + testName,
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
                );
                
                logger.info("Скриншот при падении теста сохранен в Allure отчет");
            }
        } catch (Exception e) {
            logger.error("Не удалось сделать скриншот при падении теста: {}", e.getMessage());
        }
    }
}