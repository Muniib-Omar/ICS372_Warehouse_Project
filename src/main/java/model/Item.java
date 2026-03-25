package model;
import java.io.Serializable;

public class Item implements Serializable{
    private String name;
    private int quantity;
    private double price;

    public Item(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    // Requirement 8: Calculate subtotal for this item
    public double getSubtotal() {
        return quantity * price;
    }
}
