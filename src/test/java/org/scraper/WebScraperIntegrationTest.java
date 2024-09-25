package org.scraper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scraper.client.HttpClient;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WebScraperIntegrationTest {
    private HttpClient httpClient;

    @BeforeEach
    void setup() throws Exception {
        httpClient = mock(HttpClient.class);
        // Mock the fetchUrl method to return a default response
        String defaultJsonResponse = "{\"title\": \"My Title\"}";
        String defaultHtmlResponse = "<html> <head> </head><body><h1> <class=\"product-title\" data-id=\"f3bfa24c-2645-48c8-9117-b338bef9b9ab\">Product title</h1>";
        when(httpClient.fetchUrl("http://google.com/search1.json")).thenReturn(new FetchResult(true, defaultJsonResponse, "json"));
        when(httpClient.fetchUrl("http://facebook.com/search3.json")).thenReturn(new FetchResult(true, defaultJsonResponse, "json"));
        when(httpClient.fetchUrl("http://google.com/map.html")).thenReturn(new FetchResult(true, defaultHtmlResponse, "html"));
        when(httpClient.fetchUrl("http://facebook.com/map3.html")).thenReturn(new FetchResult(true, defaultHtmlResponse, "html"));

    }

    @Test
    void testWebScraperIntegration() throws Exception {
        // Assuming you have a test file with URLs
        File testFile = new File("src/main/resources/urls.txt");
        assertTrue(testFile.exists(), "Test file does not exist");

        // Create an instance of the WebScraper
        WebScraper webScraper = new WebScraper(httpClient);

        // Start processing
        webScraper.startProcessing();
        // Verify that the HttpClient fetchUrl method was called
        verify(httpClient, atLeastOnce()).fetchUrl(anyString());
    }
}
