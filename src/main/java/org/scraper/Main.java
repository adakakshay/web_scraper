package org.scraper;

import org.scraper.client.HttpClient;
import org.scraper.config.ConfigurationLoader;

public class Main {

    public static void main(String[] args) {
        // Load configuration using Singleton pattern
        ConfigurationLoader configLoader = ConfigurationLoader.getInstance();

        // Create an HTTP client
        HttpClient httpClient = new HttpClient(configLoader.getConfig());

        // Create an instance of the WebScraper with the HttpClient
        WebScraper webScraper = new WebScraper(httpClient);

        // Start processing
        webScraper.startProcessing();
    }
}
