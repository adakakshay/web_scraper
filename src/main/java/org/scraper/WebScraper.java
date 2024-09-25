package org.scraper;

import org.scraper.config.ConfigurationLoader;
import org.scraper.processor.FileReaderProcessor;
import org.scraper.processor.UrlConsumer;
import org.scraper.client.HttpClient;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class WebScraper {
    public static void main(String[] args) {
        try {
            // Load configuration using Singleton pattern
            ConfigurationLoader configLoader = ConfigurationLoader.getInstance();
            int batchSize = configLoader.getConfig().getBatchProcessingConfig().getBatchSize();
            String filePath = "src/main/resources/urls.txt";

            // Initialize the URL queue
            BlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();

            // Initialize the FileReaderProcessor (subject)
            FileReaderProcessor fileReaderProcessor = new FileReaderProcessor(urlQueue, filePath, batchSize);

            // Create an HTTP client
            HttpClient httpClient = new HttpClient(configLoader.getConfig());

            // Create a thread pool for consumers and file processing
            ExecutorService executorService = Executors.newFixedThreadPool(1 + 5); // 1 producer + 5 consumers

            // Initialize consumers and register them as observers before starting the producer
            int numberOfConsumers = 5;
            for (int i = 0; i < numberOfConsumers; i++) {
                UrlConsumer consumer = new UrlConsumer(urlQueue, httpClient);
                fileReaderProcessor.addObserver(consumer); // Register consumer as an observer
                executorService.submit(consumer); // Submit consumers to the thread pool
            }

            // Start the FileReaderProcessor after consumers are ready
            executorService.submit(fileReaderProcessor); // Start the file reader processor

            // Shutdown executor service after tasks are complete
            executorService.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
