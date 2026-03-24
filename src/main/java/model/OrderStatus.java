package model;

public enum OrderStatus {
    INCOMING,   // Order just arrived
    FULFILLING, // Order is being prepared
    COMPLETED,  // Order is shipped/picked up
    CANCELED    // Feature 1: Order could not be fulfilled
}