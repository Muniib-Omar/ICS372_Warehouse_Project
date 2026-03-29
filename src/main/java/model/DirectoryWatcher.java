package model;

import com.group.dto.ParsedOrder;
import com.group.mapper.OrderMapper;
import com.group.parser.OrderParser;
import com.group.parser.XmlOrderParser;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DirectoryWatcher {

    private Path folderPath;

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

                key.reset();
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

            // Step 3: Add to system
            if (!realOrders.isEmpty()) {
                OrderSystem system = new OrderSystem();
                system.addOrders(realOrders);

                System.out.println("Imported " + realOrders.size() + " orders.");
            } else {
                System.out.println("No valid orders found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}