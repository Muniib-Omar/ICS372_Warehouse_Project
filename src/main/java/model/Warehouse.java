package model;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private String warehouseId;
    private List<Order> orders;

    public Warehouse(String warehouseId) {
        this.warehouseId = warehouseId;
        this.orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public String getWarehouseId() { return warehouseId; }
    public List<Order> getAllOrders() { return orders; }

    // Requirement 8: Only show uncompleted orders
    public List<Order> getUncompletedOrders() {
        List<Order> uncompleted = new ArrayList<>();
        for (Order o : orders) {
            if (o.getStatus() != OrderStatus.COMPLETED && o.getStatus() != OrderStatus.CANCELED) {
                uncompleted.add(o);
            }
        }
        return uncompleted;
    }
}