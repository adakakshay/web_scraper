package org.scraper.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class FileReaderProcessor implements Runnable {
    private final BlockingQueue<String> urlQueue;
    private final String filePath;
    private final int batchSize;

    public FileReaderProcessor(BlockingQueue<String> urlQueue, String filePath, int batchSize) {
        this.urlQueue = urlQueue;
        this.filePath = filePath;
        this.batchSize = batchSize;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<String> batch = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                batch.add(line);

                if (batch.size() == batchSize) {
                    processBatch(batch);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                processBatch(batch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processBatch(List<String> batch) {
        try {
            for (String url : batch) {
                urlQueue.put(url); // Add URLs to the queue
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
