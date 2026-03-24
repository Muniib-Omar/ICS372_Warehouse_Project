package com.group.util;

/**
 * Handles logging for parser errors.
 * Important: We log errors instead of crashing the app.
 */
public class ParseLogger {

    public static void logError(String message) {
        System.err.println("[ERROR] " + message);
    }

    public static void logInfo(String message) {
        System.out.println("[INFO] " + message);
    }
}