package model;

import java.util.HashMap;
import java.util.Map;

public class WarehouseManager {
    private static WarehouseManager instance;
    private Map<String, Warehouse> warehouses;

    private WarehouseManager() {
        warehouses = new HashMap<>();
        // Feature 1: Initialize the 3 warehouses
        warehouses.put("Warehouse_A", new Warehouse("Warehouse_A"));
        warehouses.put("Warehouse_B", new Warehouse("Warehouse_B"));
        warehouses.put("Warehouse_C", new Warehouse("Warehouse_C"));
    }

    public static WarehouseManager getInstance() {
        if (instance == null) {
            instance = new WarehouseManager();
        }
        return instance;
    }

    public Warehouse getWarehouse(String id) {
        return warehouses.get(id);
    }

    public Map<String, Warehouse> getAllWarehouses() {
        return warehouses;
    }
}