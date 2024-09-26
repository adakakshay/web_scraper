package org.scraper.processor;

import org.scraper.command.ScrappingCommand;
import org.scraper.exception.RateLimitExceededException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class UrlConsumer implements Runnable {
    private final BlockingQueue<String> urlQueue;
    private final ScrappingCommand scrappingCommand;
    private boolean terminationMessagePrinted = false;

    public UrlConsumer(BlockingQueue<String> urlQueue, ScrappingCommand scrappingCommand) {
        this.urlQueue = urlQueue;
        this.scrappingCommand = scrappingCommand;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String url = urlQueue.poll(1, TimeUnit.SECONDS);
                if (url == null) {
                    if (!terminationMessagePrinted) {
                        System.out.println("No more URLs to process, terminating consumer...");
                        terminationMessagePrinted = true;
                    }
                    break;
                }
                terminationMessagePrinted = false; // Reset flag if a URL is processed
                System.out.println("Processing URL: " + url);
                try {
                    scrappingCommand.execute(url);
                } catch (RateLimitExceededException e) {
                    System.err.println("Rate limit exceeded for URL: " + url + ", requeuing...");
                    urlQueue.put(url); // Requeue the URL if rate limit is exceeded
                } catch (RuntimeException e) {
                    System.err.println("Failed to process URL: " + url + ", requeuing...");
                    urlQueue.put(url); // Requeue the URL if a runtime exception occurs
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}