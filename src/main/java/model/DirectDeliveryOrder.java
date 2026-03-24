package model;

public class DirectDeliveryOrder extends Order {
    public DirectDeliveryOrder(String id, long date, String source) {
        super(id, OrderType.DIRECT_DELIVERY, date, source);
    }
}