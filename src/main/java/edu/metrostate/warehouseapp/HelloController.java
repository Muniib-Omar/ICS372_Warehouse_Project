package edu.metrostate.warehouseapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.*;

/**
 * Controller for the Warehouse Management Dashboard.
 * Handles UI interactions, visual indicators, and order state transitions.
 */
public class HelloController {
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, String> colId;
    @FXML private TableColumn<Order, OrderType> colType;
    @FXML private TableColumn<Order, String> colSource;
    @FXML private TableColumn<Order, Double> colPrice;
    @FXML private TableColumn<Order, OrderStatus> colStatus;
    @FXML private ComboBox<String> warehouseSelector;

    private final WarehouseManager manager = WarehouseManager.getInstance();

    @FXML
    public void initialize() {
        // 1. Link table columns to the Order class variables
        // This part tells the table WHICH data to look at
        colId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colSource.setCellValueFactory(new PropertyValueFactory<>("source"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // FIX ADDED HERE: Connects the "Type" column to the Order's type field
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        // 2. FEATURE 4: Visual Indicators for Order Type (Icons/Colors)
        // This part tells the table HOW to draw the data (Circles/Colors)
        colType.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(OrderType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Circle icon = new Circle(6);
                    if (item == OrderType.SHIP) {
                        icon.setFill(Color.DODGERBLUE); // Blue for Ship
                    } else if (item == OrderType.PICKUP) {
                        icon.setFill(Color.ORANGE);     // Orange for Pickup
                    } else {
                        icon.setFill(Color.PURPLE);     // Purple for Direct Delivery
                    }
                    setGraphic(icon);
                    setText("  " + item.toString());
                }
            }
        });

        // 3. Populate the Warehouse Selector Dropdown
        warehouseSelector.getItems().addAll(manager.getAllWarehouses().keySet());
        warehouseSelector.getSelectionModel().selectFirst();
        handleWarehouseChange(); // Refresh the table to show the test data
    }

    @FXML
    private void handleWarehouseChange() {
        String selectedWH = warehouseSelector.getValue();
        if (selectedWH != null) {
            // Connects the TableView to the ObservableList in the Warehouse model
            orderTable.setItems(manager.getWarehouse(selectedWH).getAllOrders());
        }
    }

    @FXML
    private void handleStartFulfilling() {
        Order selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = selected.startFulfilling();
            if (!success) {
                showAlert("Action Denied", "Only INCOMING orders can be started.");
            }
            orderTable.refresh();
        }
    }

    @FXML
    private void handleCompleteOrder() {
        Order selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = selected.completeOrder();
            if (!success) {
                showAlert("Action Denied", "Only FULFILLING orders can be completed.");
            }
            orderTable.refresh();
        }
    }

    @FXML
    private void handleCancelOrder() {
        Order selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.cancelOrder();
            orderTable.refresh();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}