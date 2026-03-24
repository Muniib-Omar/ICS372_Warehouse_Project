package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Order {
    private String orderId;
    private OrderType type;
    private long orderDate;
    private OrderStatus status;
    private String source; // Feature 3: Tracks if from Bullseye or WallyWorld
    private List<Item> items;

    public Order(String orderId, OrderType type, long orderDate, String source) {
        this.orderId = orderId;
        this.type = type;
        this.orderDate = orderDate;
        this.source = source;
        this.status = OrderStatus.INCOMING;
        this.items = new ArrayList<>();
    }

    // Requirement 8: Calculate the total price of the order
    public double getTotalPrice() {
        double total = 0;
        for (Item item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    // Requirement 5: Logic to prevent starting an order twice
    public boolean startFulfilling() {
        if (this.status == OrderStatus.INCOMING) {
            this.status = OrderStatus.FULFILLING;
            return true;
        }
        return false;
    }

    public boolean completeOrder() {
        if (this.status == OrderStatus.FULFILLING) {
            this.status = OrderStatus.COMPLETED;
            return true;
        }
        return false;
    }

    // Feature 1: Allow cancellation
    public void cancelOrder() {
        this.status = OrderStatus.CANCELED;
    }

    // Getters
    public void addItem(Item item) { items.add(item); }
    public String getOrderId() { return orderId; }
    public OrderType getType() { return type; }
    public OrderStatus getStatus() { return status; }
    public String getSource() { return source; }
    public List<Item> getItems() { return items; }
    public long getOrderDate() { return orderDate; }

    @Override
    public String toString() {
        return "Order ID: " + orderId + " [" + type + "] Status: " + status + " Total: $" + getTotalPrice();
    }
}