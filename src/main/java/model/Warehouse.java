package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private final String warehouseId;

    // Change: Use ObservableList so the GUI "listens" for new orders
    private final ObservableList<Order> orders;

    public Warehouse(String warehouseId) {
        this.warehouseId = warehouseId;
        // Change: Initialize as an observableArrayList
        this.orders = FXCollections.observableArrayList();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public String getWarehouseId() { return warehouseId; }

    // Change: Return ObservableList so the TableView updates automatically
    public ObservableList<Order> getAllOrders() {
        return orders;
    }

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