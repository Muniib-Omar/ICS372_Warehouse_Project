package model;
import java.nio.file.*;

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
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
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

        // TODO: call your XML parser here
        // Example:
        // List<Order> newOrders = XMLParser.parse(filePath, "WallyWorld");

        // Then add to system + save
    }
}