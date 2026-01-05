package com.saucedemo.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {
    
    @BeforeEach
    public void setUp() {
        // Базовый setup
    }
    
    @AfterEach
    public void tearDown() {
        // Базовый cleanup
    }
}
