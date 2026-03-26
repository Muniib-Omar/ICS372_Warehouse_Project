module edu.metrostate.warehouseapp {
    requires javafx.controls;
    requires javafx.fxml;

    // Add these two lines to fix the XML and JSON errors
    requires java.xml;
    requires com.google.gson;

    // Keep these lines so the GUI can see your data
    opens model to javafx.base, javafx.fxml, com.google.gson;
    exports model;

    // Add this if Person 2's parser needs to be visible
    opens com.group.parser to javafx.fxml;
    exports com.group.parser;

    opens edu.metrostate.warehouseapp to javafx.fxml;
    exports edu.metrostate.warehouseapp;
}