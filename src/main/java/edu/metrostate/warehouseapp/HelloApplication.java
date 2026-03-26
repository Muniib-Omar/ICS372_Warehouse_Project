package edu.metrostate.warehouseapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Item;
import model.PickupOrder;
import model.ShipOrder;
import model.DirectDeliveryOrder;
import model.WarehouseManager;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        WarehouseManager manager = WarehouseManager.getInstance();

        ShipOrder order1 = new ShipOrder("1001", System.currentTimeMillis(), "WallyWorld");
        order1.addItem(new Item("Test Item A", 2, 50.0));
        manager.getWarehouse("Warehouse_A").addOrder(order1);

        PickupOrder order2 = new PickupOrder("1002", System.currentTimeMillis(), "Bullseye");
        order2.addItem(new Item("Test Item B", 1, 25.0));
        manager.getWarehouse("Warehouse_B").addOrder(order2);

        DirectDeliveryOrder order3 = new DirectDeliveryOrder("1003", System.currentTimeMillis(), "WallyWorld");
        order3.addItem(new Item("Test Item C", 3, 15.0));
        manager.getWarehouse("Warehouse_C").addOrder(order3);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}