package com.group.mapper;

import com.group.dto.ParsedItem;
import com.group.dto.ParsedOrder;
import model.*;

import java.util.List;

/**
 * Maps ParsedOrder → real Order objects
 */
public class OrderMapper {

    public Order map(ParsedOrder parsedOrder) {

        String orderId = String.valueOf(parsedOrder.getOrderId());
        long orderDate = System.currentTimeMillis();
        String source = parsedOrder.getSource();

        Order order;

        // Create correct subclass (NO OrderType passed here)
        if (parsedOrder.getOrderType().equalsIgnoreCase("Delivery")) {
            order = new ShipOrder(orderId, orderDate, source);
        } else if (parsedOrder.getOrderType().equalsIgnoreCase("Pickup")) {
            order = new PickupOrder(orderId, orderDate, source);
        } else if (parsedOrder.getOrderType().equalsIgnoreCase("DirectDelivery")) {
            order = new DirectDeliveryOrder(orderId, orderDate, source);
        } else {
            throw new IllegalArgumentException("Unknown order type: " + parsedOrder.getOrderType());
        }

        // Map items
        List<ParsedItem> parsedItems = parsedOrder.getItems();

        for (ParsedItem pItem : parsedItems) {
            Item item = new Item(
                    pItem.getItemType(),
                    pItem.getQuantity(),
                    pItem.getPrice()
            );

            order.addItem(item);
        }

        return order;
    }
}