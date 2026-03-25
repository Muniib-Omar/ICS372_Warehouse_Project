package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderLogicTest {

    @Test
    void startFulfillingShouldWorkOnlyFromIncoming() {
        Order order = new ShipOrder("1001", System.currentTimeMillis(), "Bullseye");

        // New orders start as incoming
        assertEquals(OrderStatus.INCOMING, order.getStatus());

        // First start should work
        assertTrue(order.startFulfilling());
        assertEquals(OrderStatus.FULFILLING, order.getStatus());

        // Starting again should fail
        assertFalse(order.startFulfilling());
        assertEquals(OrderStatus.FULFILLING, order.getStatus());
    }

    @Test
    void completeOrderShouldFailIfOrderWasNotStarted() {
        Order order = new PickupOrder("1002", System.currentTimeMillis(), "Bullseye");

        // Cannot complete before fulfilling
        assertEquals(OrderStatus.INCOMING, order.getStatus());
        assertFalse(order.completeOrder());
        assertEquals(OrderStatus.INCOMING, order.getStatus());
    }

    @Test
    void completeOrderShouldWorkAfterStartFulfilling() {
        Order order = new DirectDeliveryOrder("1003", System.currentTimeMillis(), "WallyWorld");

        // Valid order flow
        assertTrue(order.startFulfilling());
        assertTrue(order.completeOrder());
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    void canceledOrderShouldNotStartOrComplete() {
        Order order = new ShipOrder("1004", System.currentTimeMillis(), "Bullseye");

        // Canceled orders should stay canceled
        order.cancelOrder();

        assertEquals(OrderStatus.CANCELED, order.getStatus());
        assertFalse(order.startFulfilling());
        assertFalse(order.completeOrder());
        assertEquals(OrderStatus.CANCELED, order.getStatus());
    }

    @Test
    void getTotalPriceShouldAddAllItemSubtotals() {
        Order order = new PickupOrder("1005", System.currentTimeMillis(), "Bullseye");
        order.addItem(new Item("Soap", 2, 5.25));
        order.addItem(new Item("Rubber Duck", 3, 4.00));

        // Total = 10.50 + 12.00
        assertEquals(22.50, order.getTotalPrice(), 0.0001);
    }
}