package com.group.dto;

/**
 * Represents a single item inside an order.
 * This is a simple data holder (DTO).
 */
public class ParsedItem {

    private String itemType;
    private double price;
    private int quantity;

    public ParsedItem(String itemType, double price, int quantity) {
        this.itemType = itemType;
        this.price = price;
        this.quantity = quantity;
    }

    public String getItemType() {
        return itemType;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "ParsedItem{" +
                "itemType='" + itemType + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}