package model;

import java.util.ArrayList;
import java.util.List;

public class OrderSystem {

    private List<Order> orders;

    public OrderSystem() {
        System.out.println("LOADING ORDERS...");
        orders = OrderPersistence.loadOrders();

        if (orders == null) {
            orders = new ArrayList<>();
        }
    }

    public void addOrders(List<Order> newOrders) {
        orders.addAll(newOrders);
        OrderPersistence.saveOrders(orders);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
