package model;

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
        File file = new File(FILE_NAME);

        // If file doesn't exist, return empty list (first run case)
        if (!file.exists()) {
            System.out.println("No existing orders file found. Starting fresh.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {

            return (List<Order>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}