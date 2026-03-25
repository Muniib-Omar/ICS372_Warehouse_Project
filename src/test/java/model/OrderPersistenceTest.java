package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderPersistenceTest {

    @AfterEach
    void cleanup() {
        // Remove saved test file after each test
        File file = new File("orders.dat");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void saveAndLoadOrdersShouldRestoreData() {
        List<Order> originalOrders = new ArrayList<>();

        Order order1 = new ShipOrder("2001", 111111111L, "Bullseye");
        order1.addItem(new Item("Soap", 2, 5.25));
        order1.startFulfilling();

        Order order2 = new DirectDeliveryOrder("2002", 222222222L, "WallyWorld");
        order2.addItem(new Item("Paper Towel Roll", 1, 8.99));
        order2.cancelOrder();

        originalOrders.add(order1);
        originalOrders.add(order2);

        // Save then load back
        OrderPersistence.saveOrders(originalOrders);
        List<Order> loadedOrders = OrderPersistence.loadOrders();

        assertEquals(2, loadedOrders.size());

        Order loaded1 = loadedOrders.get(0);
        Order loaded2 = loadedOrders.get(1);

        // Check first order
        assertEquals("2001", loaded1.getOrderId());
        assertEquals(OrderType.SHIP, loaded1.getType());
        assertEquals(OrderStatus.FULFILLING, loaded1.getStatus());
        assertEquals("Bullseye", loaded1.getSource());
        assertEquals(1, loaded1.getItems().size());

        // Check second order
        assertEquals("2002", loaded2.getOrderId());
        assertEquals(OrderType.DIRECT_DELIVERY, loaded2.getType());
        assertEquals(OrderStatus.CANCELED, loaded2.getStatus());
        assertEquals("WallyWorld", loaded2.getSource());
        assertEquals(1, loaded2.getItems().size());
    }

    @Test
    void loadOrdersShouldReturnEmptyListIfFileDoesNotExist() {
        File file = new File("orders.dat");
        if (file.exists()) {
            file.delete();
        }

        // Missing save file should not crash
        List<Order> loadedOrders = OrderPersistence.loadOrders();

        assertNotNull(loadedOrders);
        assertTrue(loadedOrders.isEmpty());
    }
}