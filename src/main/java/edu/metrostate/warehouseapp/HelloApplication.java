package edu.metrostate.warehouseapp;

import com.group.dto.ParsedOrder;
import com.group.mapper.OrderMapper;
import com.group.parser.XmlOrderParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DirectoryWatcher;
import model.Order;
import model.OrderSystem;
import model.WarehouseManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        WarehouseManager manager = WarehouseManager.getInstance();
        OrderSystem orderSystem = new OrderSystem();

        // Step 1: Load previously saved orders into the UI
        List<Order> savedOrders = orderSystem.getOrders();
        for (Order order : savedOrders) {
            manager.getWarehouse("Warehouse_A").addOrder(order);
        }

        // Step 2: If no saved orders exist yet, import ExampleOrder1.xml once
        if (savedOrders.isEmpty()) {
            try {
                XmlOrderParser parser = new XmlOrderParser();
                OrderMapper mapper = new OrderMapper();

                File file = new File("src/main/resources/ExampleOrder1.xml");
                List<ParsedOrder> parsedOrders = parser.parse(file);

                List<Order> importedOrders = new ArrayList<>();

                for (ParsedOrder parsedOrder : parsedOrders) {
                    Order order = mapper.map(parsedOrder);
                    importedOrders.add(order);
                    manager.getWarehouse("Warehouse_A").addOrder(order);
                }

                if (!importedOrders.isEmpty()) {
                    orderSystem.addOrders(importedOrders);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Step 3: Start directory watcher in background
        try {
            String watchFolder = Paths.get("src/main/resources").toAbsolutePath().toString();

            DirectoryWatcher watcher = new DirectoryWatcher(watchFolder);
            Thread watcherThread = new Thread(watcher::startWatching);
            watcherThread.setDaemon(true);
            watcherThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Warehouse Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}