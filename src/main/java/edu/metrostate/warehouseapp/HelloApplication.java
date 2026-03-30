package edu.metrostate.warehouseapp;

import com.group.dto.ParsedOrder;
import com.group.mapper.OrderMapper;
import com.group.parser.XmlOrderParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Order;
import model.WarehouseManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        WarehouseManager manager = WarehouseManager.getInstance();

        try {
            XmlOrderParser parser = new XmlOrderParser();
            OrderMapper mapper = new OrderMapper();

            File file = new File("src/main/resources/ExampleOrder1.xml");
            List<ParsedOrder> parsedOrders = parser.parse(file);

            for (ParsedOrder parsedOrder : parsedOrders) {
                Order order = mapper.map(parsedOrder);
                manager.getWarehouse("Warehouse_A").addOrder(order);
            }

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