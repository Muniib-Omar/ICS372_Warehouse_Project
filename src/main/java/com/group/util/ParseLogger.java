package com.group.util;

/**
 * Handles logging for parser errors.
 * Important: We log errors instead of crashing the app.
 */

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class ParseLogger {

    public static void logError(String message) {
        System.err.println("[ERROR] " + message);

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
        });
    }

    public static void logInfo(String message) {
        System.out.println("[INFO] " + message);
    }
}


