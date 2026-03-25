package com.group.util;

import model.OrderType;

// Simple checks for input values before adding data
public class Validator {

    public static boolean isValidOrderId(String orderId) {
        return orderId != null && !orderId.trim().isEmpty();
    }

    public static boolean isValidItemName(String itemName) {
        return itemName != null && !itemName.trim().isEmpty();
    }

    public static boolean isValidPrice(double price) {
        return price >= 0;
    }

    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

    public static boolean isValidOrderType(OrderType type) {
        return type != null;
    }

    public static boolean isValidSource(String source) {
        return source != null && !source.trim().isEmpty();
    }
}