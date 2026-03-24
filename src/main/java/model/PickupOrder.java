package model;

public class PickupOrder extends Order {
    public PickupOrder(String id, long date, String source) {
        super(id, OrderType.PICKUP, date, source);
    }
}