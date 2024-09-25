package org.scraper.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.scraper.FetchResult;
import org.scraper.config.ApiConfig;
import org.scraper.config.ApiRateLimitConfig;
import org.scraper.config.DomainConfig;
import org.scraper.config.WebScrapperConfig;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class HttpClientTest {
    private WebScrapperConfig config;
    private DomainConfig domainConfig;
    private ApiConfig apiConfig;
    private ApiRateLimitConfig rateLimitConfig;
    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        // Mock the configuration objects
        config = mock(WebScrapperConfig.class);
        domainConfig = mock(DomainConfig.class);
        apiConfig = mock(ApiConfig.class);
        rateLimitConfig = mock(ApiRateLimitConfig.class);

        // Set up the configuration behavior
        when(config.getDomainConfigOrDefault(anyString())).thenReturn(domainConfig);
        when(domainConfig.getApiConfig()).thenReturn(new HashMap<String, ApiConfig>() {{
            put("search1.json", apiConfig);
        }});
        when(apiConfig.getContentType()).thenReturn("json");
        when(apiConfig.getRateLimitConfig()).thenReturn(rateLimitConfig);

        // Initialize the HttpClient with the mocked configuration
        httpClient = new HttpClient(config);
    }

    @Test
    void testFetchUrl() throws Exception {
        // Mock the fetch method to return a successful FetchResult
        HttpClient spyHttpClient = Mockito.spy(httpClient);
        FetchResult expectedFetchResult = new FetchResult(true, "{\"title\": \"My Title\"}", "json");
        doReturn(expectedFetchResult).when(spyHttpClient).fetch(anyString(), anyString());

        // Call the fetchUrl method
        FetchResult actualFetchResult = spyHttpClient.fetchUrl("http://example.com/search1.json");

        // Verify the fetch method was called with the correct parameters
        verify(spyHttpClient).fetch("http://example.com/search1.json", "json");

        // Assert the fetch result
        assertEquals(expectedFetchResult, actualFetchResult);
    }
}