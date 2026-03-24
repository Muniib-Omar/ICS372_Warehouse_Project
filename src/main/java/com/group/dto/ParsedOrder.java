package com.group.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an order parsed from XML or JSON.
 * This is temporary until we connect to Person 1's Order classes.
 */
public class ParsedOrder {

    private int orderId;
    private String orderType;
    private String source;
    private List<ParsedItem> items;

    public ParsedOrder(int orderId, String orderType, String source) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.source = source;
        this.items = new ArrayList<>();
    }

    public void addItem(ParsedItem item) {
        items.add(item);
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getSource() {
        return source;
    }

    public List<ParsedItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "ParsedOrder{" +
                "orderId=" + orderId +
                ", orderType='" + orderType + '\'' +
                ", source='" + source + '\'' +
                ", items=" + items +
                '}';
    }
}