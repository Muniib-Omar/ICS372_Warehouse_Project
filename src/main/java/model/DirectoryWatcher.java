package model;

import com.group.dto.ParsedOrder;
import com.group.mapper.OrderMapper;
import com.group.parser.OrderParser;
import com.group.parser.XmlOrderParser;
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
        try (WatchService watchService =
                     FileSystems.getDefault().newWatchService()) {

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
            e.printStackTrace();
        }
    }

    private void processFile(Path filePath) {
        System.out.println("Processing: " + filePath);

        try {
            String fileName = filePath.toString().toLowerCase();

            if (!fileName.endsWith(".xml")) {
                System.out.println("Only XML files supported.");
                return;
            }

            // Step 1: Parse XML
            OrderParser parser = new XmlOrderParser();
            List<ParsedOrder> parsedOrders = parser.parse(filePath.toFile());

            // Step 2: Map to real Orders
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
                System.out.println("No valid orders found.");
                return;
            }

            // Step 3: Save to persistence
            OrderSystem system = new OrderSystem();
            system.addOrders(realOrders);

            // Step 4: Show in UI by adding to Warehouse_A
            WarehouseManager manager = WarehouseManager.getInstance();
            Platform.runLater(() -> {
                for (Order order : realOrders) {
                    manager.getWarehouse("Warehouse_A").addOrder(order);
                }
            });

            System.out.println("Imported " + realOrders.size() + " orders.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}