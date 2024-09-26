package org.scraper;

import org.scraper.client.HttpClientService;
import org.scraper.command.BasicScrapingCommand;
import org.scraper.command.ScrappingCommand;
import org.scraper.processor.FileReaderProcessor;
import org.scraper.processor.UrlConsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class WebScraper {
    private final HttpClientService httpClientService;

    public WebScraper(HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }
    public void startProcessing() {
        try {

            String filePath = "src/main/resources/urls.txt";

            // Initialize the URL queue
            BlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();

            // Create a ScrappingCommand
            ScrappingCommand scrappingCommand = new BasicScrapingCommand(httpClientService);

            // Define the number of consumers
            int numberOfConsumers = 5;

            // Create a thread pool
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfConsumers); // 1 producer + 5 consumers

            // Start consumers (independently)
            for (int i = 0; i < numberOfConsumers; i++) {
                UrlConsumer consumer = new UrlConsumer(urlQueue, scrappingCommand);
                executorService.submit(consumer); // Submit each consumer to the thread pool
            }

            // Create a thread pool
            ExecutorService producerExecutorService = Executors.newFixedThreadPool(1);

            // Initialize the FileReaderProcessor
            FileReaderProcessor fileReaderProcessor = new FileReaderProcessor(urlQueue, filePath);

            // Submit FileReaderProcessor to the executor service to produce URLs
            producerExecutorService.submit(fileReaderProcessor);

            // Wait for file processing and consumers to finish
            executorService.shutdown();
            producerExecutorService.shutdown();
            if (!executorService.awaitTermination(10, TimeUnit.MINUTES) ||
                    !producerExecutorService.awaitTermination(10, TimeUnit.MINUTES)) {
                executorService.shutdownNow(); // Force shutdown if it takes too long
                producerExecutorService.shutdown();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}