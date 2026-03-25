package com.group.util;

import model.OrderType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    @Test
    void validOrderIdShouldPass() {
        // Valid ID should pass, blank values should fail
        assertTrue(Validator.isValidOrderId("12345"));
        assertFalse(Validator.isValidOrderId(null));
        assertFalse(Validator.isValidOrderId(""));
        assertFalse(Validator.isValidOrderId("   "));
    }

    @Test
    void validItemNameShouldPass() {
        // Item name must not be blank
        assertTrue(Validator.isValidItemName("Soap"));
        assertFalse(Validator.isValidItemName(null));
        assertFalse(Validator.isValidItemName(""));
        assertFalse(Validator.isValidItemName("   "));
    }

    @Test
    void validPriceShouldPass() {
        // Price can be zero or more
        assertTrue(Validator.isValidPrice(0.0));
        assertTrue(Validator.isValidPrice(4.99));
        assertFalse(Validator.isValidPrice(-1.0));
    }

    @Test
    void validQuantityShouldPass() {
        // Quantity must be greater than 0
        assertTrue(Validator.isValidQuantity(1));
        assertTrue(Validator.isValidQuantity(10));
        assertFalse(Validator.isValidQuantity(0));
        assertFalse(Validator.isValidQuantity(-2));
    }

    @Test
    void validOrderTypeShouldPass() {
        // Only real order types should pass
        assertTrue(Validator.isValidOrderType(OrderType.SHIP));
        assertTrue(Validator.isValidOrderType(OrderType.PICKUP));
        assertTrue(Validator.isValidOrderType(OrderType.DIRECT_DELIVERY));
        assertFalse(Validator.isValidOrderType(null));
    }

    @Test
    void validSourceShouldPass() {
        // Source must not be blank
        assertTrue(Validator.isValidSource("Bullseye"));
        assertTrue(Validator.isValidSource("WallyWorld"));
        assertFalse(Validator.isValidSource(null));
        assertFalse(Validator.isValidSource(""));
        assertFalse(Validator.isValidSource("   "));
    }
}