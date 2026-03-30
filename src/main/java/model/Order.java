package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The base Order class.
 * Note: Must be public so JavaFX TableView can access the getters via reflection.
 */
public abstract class Order implements Serializable {
    private String orderId;
    private OrderType type;
    private long orderDate;
    private OrderStatus status;
    private String source;
    private List<Item> items;

    public Order(String orderId, OrderType type, long orderDate, String source) {
        this.orderId = orderId;
        this.type = type;
        this.orderDate = orderDate;
        this.source = source;
        this.status = OrderStatus.INCOMING;
        this.items = new ArrayList<>();
    }

    // Calculate the total price of the order
    public double getTotalPrice() {
        double total = 0;
        for (Item item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    // Only INCOMING orders can start fulfilling
    public boolean startFulfilling() {
        if (this.status == OrderStatus.INCOMING) {
            this.status = OrderStatus.FULFILLING;
            return true;
        }
        return false;
    }

    // Only FULFILLING orders can be completed
    public boolean completeOrder() {
        if (this.status == OrderStatus.FULFILLING) {
            this.status = OrderStatus.COMPLETED;
            return true;
        }
        return false;
    }

    // Only INCOMING or FULFILLING orders can be canceled
    public boolean cancelOrder() {
        if (this.status == OrderStatus.INCOMING || this.status == OrderStatus.FULFILLING) {
            this.status = OrderStatus.CANCELED;
            return true;
        }
        return false;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderType getType() {
        return type;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public List<Item> getItems() {
        return items;
    }

    public long getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + " [" + type + "] Status: " + status + " Total: $" + getTotalPrice();
    }
}