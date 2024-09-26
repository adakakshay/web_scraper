package org.scraper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scraper.client.HttpClientService;
import org.scraper.config.ConfigurationLoader;

import java.io.File;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WebScraperIntegrationTest {
    private HttpClient httpClient;
    private HttpClientService httpClientService;

    @BeforeEach
    void setup() throws Exception {
        httpClient = mock(HttpClient.class);
        httpClientService = new HttpClientService(ConfigurationLoader.getInstance().getConfig(), httpClient);

        // Mock the HttpResponse
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn("{\"title\": \"My Title\"}");
        when(mockResponse.headers()).thenReturn(HttpHeaders.of(Map.of("Content-Type", List.of("application/json")), (k, v) -> true));
        when(mockResponse.statusCode()).thenReturn(200);

        // Mock the HttpClient to return the mockResponse
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);
    }

    @Test
    void testWebScraperIntegration() throws Exception {
        // Assuming you have a test file with URLs
        File testFile = new File("src/main/resources/urls.txt");
        assertTrue(testFile.exists(), "Test file does not exist");

        // Create an instance of the WebScraper
        WebScraper webScraper = new WebScraper(httpClientService);

        // Start processing
        webScraper.startProcessing();

        // Verify that the HttpClient send method was called
        verify(httpClient, atLeastOnce()).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }
}
