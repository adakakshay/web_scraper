package org.scraper;

import org.scraper.client.HttpClientService;
import org.scraper.config.ConfigurationLoader;

import java.net.http.HttpClient;

public class Main {

    public static void main(String[] args) {
        // Load configuration using Singleton pattern
        ConfigurationLoader configLoader = ConfigurationLoader.getInstance();

        // Create an HTTP client
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpClientService httpClientService = new HttpClientService(configLoader.getConfig(), httpClient);

        // Create an instance of the WebScraper with the HttpClientService
        WebScraper webScraper = new WebScraper(httpClientService);

        // Start processing
        webScraper.startProcessing();
    }
}
