module edu.metrostate.warehouseapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.metrostate.warehouseapp to javafx.fxml;
    exports edu.metrostate.warehouseapp;
}