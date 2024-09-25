package org.scraper.processor;

import org.scraper.FetchResult;
import org.scraper.client.HttpClient;
import org.scraper.observer.UrlObserver;

import java.util.concurrent.BlockingQueue;

public class UrlConsumer implements Runnable, UrlObserver {
    private final BlockingQueue<String> urlQueue;
    private final HttpClient httpClient;

    public UrlConsumer(BlockingQueue<String> urlQueue, HttpClient httpClient) {
        this.urlQueue = urlQueue;
        this.httpClient = httpClient;
    }

    @Override
    public void run() {
        while (true) {
            processUrl();
        }
    }

    private void processUrl() {
        String url = null;
        try {
            url = urlQueue.take(); // Take the URL from the queue
            System.out.println("Processing URL: " + url);
            FetchResult fetchResult = httpClient.fetchUrl(url); // Fetch the URL using the HTTP client
            System.out.println("Response: " + fetchResult.isSuccess());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // If rate limit exceeded, re-add URL to the queue after some delay
            System.err.println("Failed to process URL: " + e.getMessage());
            if (url != null) {
                try {
                    Thread.sleep(1000); // Delay before re-adding
                    urlQueue.put(url); // Re-add URL to the queue
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void update() {
        // If notified of a new URL, process it
        System.out.println("New URL available to process: ");
        processUrl();
    }
}
