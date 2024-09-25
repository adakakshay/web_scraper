package org.scraper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.scraper.client.HttpClient;
import org.scraper.config.ConfigurationLoader;
import org.scraper.processor.FileReaderProcessor;
import org.scraper.processor.UrlConsumer;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WebScraperIntegrationTest {
    private ConfigurationLoader configLoader;
    private HttpClient httpClient;
    private BlockingQueue<String> urlQueue;
    private FileReaderProcessor fileReaderProcessor;
    private ExecutorService executorService;

    @BeforeEach
    public void setUp() throws IOException {
        // Load the actual configuration
        configLoader = ConfigurationLoader.getInstance();

        // Mock the HttpClient
        httpClient = Mockito.mock(HttpClient.class);

        // Initialize the URL queue
        urlQueue = new LinkedBlockingQueue<>();

        // Initialize the FileReaderProcessor (subject)
        fileReaderProcessor = new FileReaderProcessor(urlQueue, "src/main/resources/urls.txt", 5);

        // Create a thread pool for the file reader processor
        executorService = Executors.newFixedThreadPool(1);
    }

    @Test
    public void testWebScraperIntegration() throws Exception {
        // Mock the fetchUrl method to return a default response
        String defaultJsonResponse = "{\"title\": \"My Title\"}";
        String defaultHtmlResponse = "<html> <head> </head><body><h1> <class=\"product-title\" data-id=\"f3bfa24c-2645-48c8-9117-b338bef9b9ab\">Product title</h1>";
        when(httpClient.fetchUrl("http://google.com/entity-slug-uuid1.json")).thenReturn(new FetchResult(true, defaultJsonResponse));
        when(httpClient.fetchUrl("http://example.com/entity-slug-uuid2.json")).thenReturn(new FetchResult(false, defaultJsonResponse));
        when(httpClient.fetchUrl("http://google2.com/product-slug3.html")).thenReturn(new FetchResult(true, defaultHtmlResponse));
        when(httpClient.fetchUrl("http://google2.com/product-slug2.html")).thenReturn(new FetchResult(false, defaultHtmlResponse));
        // Create consumers (observers) and register them
        int numberOfConsumers = 5;
        for (int i = 0; i < numberOfConsumers; i++) {
            UrlConsumer consumer = new UrlConsumer(urlQueue, httpClient);
            fileReaderProcessor.addObserver(consumer); // Register consumer as an observer
        }

        // Start the file reader processor
        executorService.submit(fileReaderProcessor);

        // Start consumers
        for (int i = 0; i < numberOfConsumers; i++) {
            executorService.submit(new UrlConsumer(urlQueue, httpClient));
        }

        // Wait for the tasks to complete
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            // Wait for all tasks to finish
        }

        // Verify that the HttpClient fetchUrl method was called
        verify(httpClient, atLeastOnce()).fetchUrl(anyString());
    }
}