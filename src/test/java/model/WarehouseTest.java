package model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {

    @Test
    void getUncompletedOrdersShouldExcludeCompletedAndCanceledOrders() {
        Warehouse warehouse = new Warehouse("Warehouse_A");

        Order incomingOrder = new ShipOrder("3001", System.currentTimeMillis(), "Bullseye");
        Order fulfillingOrder = new PickupOrder("3002", System.currentTimeMillis(), "Bullseye");
        Order completedOrder = new DirectDeliveryOrder("3003", System.currentTimeMillis(), "WallyWorld");
        Order canceledOrder = new ShipOrder("3004", System.currentTimeMillis(), "Bullseye");

        // Set different statuses
        fulfillingOrder.startFulfilling();
        completedOrder.startFulfilling();
        completedOrder.completeOrder();
        canceledOrder.cancelOrder();

        warehouse.addOrder(incomingOrder);
        warehouse.addOrder(fulfillingOrder);
        warehouse.addOrder(completedOrder);
        warehouse.addOrder(canceledOrder);

        List<Order> uncompleted = warehouse.getUncompletedOrders();

        // Only incoming and fulfilling should stay
        assertEquals(2, uncompleted.size());
        assertTrue(uncompleted.contains(incomingOrder));
        assertTrue(uncompleted.contains(fulfillingOrder));
        assertFalse(uncompleted.contains(completedOrder));
        assertFalse(uncompleted.contains(canceledOrder));
    }

    @Test
    void warehouseManagerShouldInitializeThreeWarehouses() {
        WarehouseManager manager = WarehouseManager.getInstance();

        // Check default warehouses
        assertNotNull(manager.getWarehouse("Warehouse_A"));
        assertNotNull(manager.getWarehouse("Warehouse_B"));
        assertNotNull(manager.getWarehouse("Warehouse_C"));
        assertEquals(3, manager.getAllWarehouses().size());
    }
}