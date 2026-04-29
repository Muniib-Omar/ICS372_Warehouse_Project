package model;

import com.group.dto.ParsedOrder;
import com.group.mapper.OrderMapper;
import com.group.parser.JsonOrderParser;
import com.group.parser.OrderParser;
import com.group.parser.XmlOrderParser;
import com.group.util.ParseLogger;
import javafx.application.Platform;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DirectoryWatcher {

    private final Path folderPath;

    public DirectoryWatcher(String folder) {
        this.folderPath = Paths.get(folder);
    }

    public void startWatching() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {

            folderPath.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE
            );

            System.out.println("Watching folder: " + folderPath);

            while (true) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path fileName = (Path) event.context();
                        System.out.println("New file detected: " + fileName);

                        processFile(folderPath.resolve(fileName));
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    System.out.println("Watch key is no longer valid. Stopping watcher.");
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Watcher stopped: " + e.getMessage());
        }
    }

    private void processFile(Path filePath) {
        System.out.println("Processing: " + filePath);

        try {
            String fileName = filePath.toString().toLowerCase();

            OrderParser parser;

            if (fileName.endsWith(".xml")) {
                parser = new XmlOrderParser();
            } else if (fileName.endsWith(".json")) {
                parser = new JsonOrderParser();
            } else {
                ParseLogger.logError("Unsupported file type: " + filePath.getFileName());
                return;
            }

            // Step 1: Parse XML or JSON file
            List<ParsedOrder> parsedOrders = parser.parse(filePath.toFile());

            // Step 2: Convert ParsedOrder objects into real Order objects
            OrderMapper mapper = new OrderMapper();
            List<Order> realOrders = new ArrayList<>();

            for (ParsedOrder parsed : parsedOrders) {
                try {
                    realOrders.add(mapper.map(parsed));
                } catch (Exception e) {
                    System.out.println("Skipping bad parsed order: " + e.getMessage());
                }
            }

            if (realOrders.isEmpty()) {
                ParseLogger.logError("No valid orders found.");
                return;
            }

            // Step 3: Save imported orders
            OrderSystem system = new OrderSystem();
            system.addOrders(realOrders);

            // Step 4: Add imported orders to the UI warehouse
            WarehouseManager manager = WarehouseManager.getInstance();

            Platform.runLater(() -> {
                for (Order order : realOrders) {
                    // Distribute orders across warehouses (based on orderId)
                    String[] warehouseIds = {"Warehouse_A", "Warehouse_B", "Warehouse_C"};
                    int index = Math.abs(order.getOrderId().hashCode()) % warehouseIds.length;

                    manager.getWarehouse(warehouseIds[index]).addOrder(order);

                }

                ParseLogger.logInfo("Imported " + realOrders.size() + " orders.");
            });

        } catch (Exception e) {
            ParseLogger.logError("Import failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}