package org.scraper;

import org.scraper.command.BasicScrapingCommand;
import org.scraper.command.ScrappingCommand;
import org.scraper.config.ConfigurationLoader;
import org.scraper.processor.FileReaderProcessor;
import org.scraper.processor.UrlConsumer;
import org.scraper.client.HttpClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class WebScraper {
    private final HttpClient httpClient;

    public WebScraper(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void startProcessing() {
        try {
            // Load configuration using Singleton pattern
            ConfigurationLoader configLoader = ConfigurationLoader.getInstance();
            int batchSize = configLoader.getConfig().getBatchProcessingConfig().getBatchSize();
            String filePath = "src/main/resources/urls.txt";

            // Initialize the URL queue
            BlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();

            // Initialize the FileReaderProcessor (producer)
            FileReaderProcessor fileReaderProcessor = new FileReaderProcessor(urlQueue, filePath, batchSize);

            // Create a ScrappingCommand
            ScrappingCommand scrappingCommand = new BasicScrapingCommand(httpClient);

            // Define the number of consumers
            int numberOfConsumers = 5;

            // Create a thread pool
            ExecutorService executorService = Executors.newFixedThreadPool(1 + numberOfConsumers); // 1 producer + 5 consumers

            // Submit FileReaderProcessor to the executor service to produce URLs
            executorService.submit(fileReaderProcessor);

            // Start consumers (independently)
            for (int i = 0; i < numberOfConsumers; i++) {
                UrlConsumer consumer = new UrlConsumer(urlQueue, scrappingCommand);
                executorService.submit(consumer); // Submit each consumer to the thread pool
            }

            // Wait for file processing and consumers to finish
            executorService.shutdown();
            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                executorService.shutdownNow(); // Force shutdown if it takes too long
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}