package org.scraper.processor;

import org.scraper.observer.UrlObserver;
import org.scraper.observer.UrlSubject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class FileReaderProcessor implements Runnable, UrlSubject {
    private final BlockingQueue<String> urlQueue;
    private final String filePath;
    private final int batchSize;
    private final List<UrlObserver> observers = new ArrayList<>();

    public FileReaderProcessor(BlockingQueue<String> urlQueue, String filePath, int batchSize) {
        this.urlQueue = urlQueue;
        this.filePath = filePath;
        this.batchSize = batchSize;
    }

    @Override
    public void addObserver(UrlObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (UrlObserver observer : observers) {
            observer.update();
        }
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
            // Notify observers that a new URL is available
            notifyObservers();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
