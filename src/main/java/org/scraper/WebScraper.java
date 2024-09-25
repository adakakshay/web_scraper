package org.scraper;

import org.scraper.command.BasicScrapingCommand;
import org.scraper.config.ConfigurationLoader;
import org.scraper.config.RateLimitConfig;
import org.scraper.processor.BatchProcessor;
import org.scraper.processor.UrlConsumer;
import org.scraper.observer.LoggingObserver;
import org.scraper.observer.UrlProcessingNotifier;
import org.scraper.limiter.DefaultRateLimitingStrategy;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebScraper {
    public static void main(String[] args) {
        try {
            // Load configuration using Singleton pattern
            ConfigurationLoader configLoader = ConfigurationLoader.getInstance();

            // Initialize notifier and observers
            UrlProcessingNotifier notifier = new UrlProcessingNotifier();
            notifier.addObserver(new LoggingObserver());

            // Initialize rate limiter using Strategy pattern
            DefaultRateLimitingStrategy rateLimiter = new DefaultRateLimitingStrategy(rateLimitConfig);

            // Initialize batch processor
            BatchProcessor batchProcessor = new BatchProcessor(10, 100);

            // Create a thread pool for URL consumers
            int numberOfConsumers = 5;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfConsumers);

            // Start consumers
            for (int i = 0; i < numberOfConsumers; i++) {
                executorService.submit(new UrlConsumer(batchProcessor.getUrlQueue(), rateLimiter, notifier, new BasicScrapingCommand()));
            }

            // Load URLs to process and publish them
            GenericFileLoader fileLoader = new GenericFileLoader();
            List<String> urls = fileLoader.loadTextFile("src/main/resources/urls.txt");
            batchProcessor.publishUrls(urls);


            // Shutdown executor service
            executorService.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
