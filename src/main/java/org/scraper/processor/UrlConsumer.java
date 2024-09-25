package org.scraper.processor;

import org.scraper.FetchResult;
import org.scraper.client.HttpClient;
import org.scraper.command.ScrappingCommand;
import org.scraper.factory.ResponseHandlerFactory;
import org.scraper.factory.handler.URLResponseHandler;

import java.util.concurrent.BlockingQueue;

public class UrlConsumer implements Runnable {
    private final BlockingQueue<String> urlQueue;
    private final ScrappingCommand scrappingCommand;


    public UrlConsumer(BlockingQueue<String> urlQueue, ScrappingCommand scrappingCommand) {
        this.urlQueue = urlQueue;
        this.scrappingCommand = scrappingCommand;
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

            //Command Factory to execute
            scrappingCommand.execute(url);

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
}
