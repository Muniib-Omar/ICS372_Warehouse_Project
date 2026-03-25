package model;

import java.util.List;

public class OrderSystem {

    private List<Order> orders;

    public OrderSystem() {
        // Load saved orders on startup
        System.out.println("LOADING ORDERS..."); //Test if constructor runs
        orders = OrderPersistence.loadOrders();
    }

    public void addOrders(List<Order> newOrders) {
        orders.addAll(newOrders);

        // Save immediately after change
        OrderPersistence.saveOrders(orders);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
