package model;

public class ShipOrder extends Order {
    public ShipOrder(String id, long date, String source) {
        super(id, OrderType.SHIP, date, source);
    }
}