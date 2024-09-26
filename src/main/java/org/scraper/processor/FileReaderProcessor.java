// src/main/java/org/scraper/processor/FileReaderProcessor.java
package org.scraper.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class FileReaderProcessor implements Runnable {
    private final BlockingQueue<String> urlQueue;
    private final String filePath;

    public FileReaderProcessor(BlockingQueue<String> urlQueue, String filePath) {
        this.urlQueue = urlQueue;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                urlQueue.put(line); // Add each URL to the queue directly
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}