package org.scraper.processor;

import org.scraper.observer.UrlProcessingNotifier;

import java.util.concurrent.BlockingQueue;

public class UrlConsumer implements Runnable {
    private final BlockingQueue<String> urlQueue;
    private final UrlProcessingNotifier notifier;
    private final DomainProcessor domainProcessor;

    public UrlConsumer(BlockingQueue<String> urlQueue, UrlProcessingNotifier notifier, DomainProcessor domainProcessor) {
        this.urlQueue = urlQueue;
        this.notifier = notifier;
        this.domainProcessor = domainProcessor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Take a URL from the queue
                String url = urlQueue.take();

                // Process the URL
                domainProcessor.process(url);

                // Notify observers about the processed URL and result
                notifier.notifyObservers(url, "Success");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                // Notify observers about the failure
                notifier.notifyObservers("Failed to process URL: " + e.getMessage(), null);
            }
        }
    }
}