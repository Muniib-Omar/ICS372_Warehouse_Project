package edu.metrostate.warehouseapp;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.*;

import java.util.ArrayList;

/**
 * Controller for the Warehouse Management Dashboard.
 * Handles UI interactions, visual indicators, order state transitions,
 * item viewing, and manual order export.
 */
public class HelloController {
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, String> colId;
    @FXML private TableColumn<Order, OrderType> colType;
    @FXML private TableColumn<Order, String> colSource;
    @FXML private TableColumn<Order, Double> colPrice;
    @FXML private TableColumn<Order, OrderStatus> colStatus;
    @FXML private ComboBox<String> warehouseSelector;

    // Displays the items for whichever order is currently selected.
    @FXML private TableView<Item> itemTable;
    @FXML private TableColumn<Item, String> colItemName;
    @FXML private TableColumn<Item, Integer> colItemQuantity;
    @FXML private TableColumn<Item, Double> colItemPrice;
    @FXML private TableColumn<Item, Double> colItemSubtotal;

    private final WarehouseManager manager = WarehouseManager.getInstance();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colSource.setCellValueFactory(new PropertyValueFactory<>("source"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Connects item fields to the item detail table columns.
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colItemSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Updates the item table whenever the user selects a different order.
        orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldOrder, newOrder) -> {
            if (newOrder == null) {
                itemTable.getItems().clear();
            } else {
                itemTable.setItems(FXCollections.observableArrayList(newOrder.getItems()));
            }
        });

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
                        icon.setFill(Color.DODGERBLUE);
                    } else if (item == OrderType.PICKUP) {
                        icon.setFill(Color.ORANGE);
                    } else {
                        icon.setFill(Color.PURPLE);
                    }
                    setGraphic(icon);
                    setText("  " + item.toString());
                }
            }
        });

        warehouseSelector.getItems().addAll(manager.getAllWarehouses().keySet());
        warehouseSelector.getSelectionModel().selectFirst();
        handleWarehouseChange();
    }

    @FXML
    private void handleWarehouseChange() {
        String selectedWH = warehouseSelector.getValue();
        if (selectedWH != null) {
            orderTable.setItems(manager.getWarehouse(selectedWH).getAllOrders());
            itemTable.getItems().clear();
        }
    }

    @FXML
    private void handleStartFulfilling() {
        Order selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = selected.startFulfilling();
            if (!success) {
                showAlert("Action Denied", "Only INCOMING orders can be started.");
            } else {
                saveCurrentWarehouseOrders();
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
            } else {
                saveCurrentWarehouseOrders();
            }
            orderTable.refresh();
        }
    }

    @FXML
    private void handleCancelOrder() {
        Order selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = selected.cancelOrder();
            if (!success) {
                showAlert("Action Denied", "Completed or already canceled orders cannot be canceled.");
            } else {
                saveCurrentWarehouseOrders();
            }
            orderTable.refresh();
        }
    }

    // Manually exports the currently selected warehouse's orders.
    @FXML
    private void handleExportOrders() {
        saveCurrentWarehouseOrders();
        showInfo("Export Complete", "Current warehouse orders were exported successfully.");
    }

    private void saveCurrentWarehouseOrders() {
        String selectedWH = warehouseSelector.getValue();
        if (selectedWH != null) {
            OrderPersistence.saveOrders(
                    new ArrayList<>(manager.getWarehouse(selectedWH).getAllOrders())
            );
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Shows success messages for completed UI actions.
    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

