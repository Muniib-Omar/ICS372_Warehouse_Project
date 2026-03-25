import persistence.OrderPersistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderPersistence {

    private static final String FILE_NAME = "orders.dat";

    // SAVE
    public static void saveOrders(List<Order> orders) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(orders);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // LOAD
    public static List<Order> loadOrders() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            return (List<Order>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();  
            return new ArrayList<>();
        }
    }
}
