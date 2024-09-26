package org.scraper.client;

import org.scraper.DomainUtils;
import org.scraper.FetchResult;
import org.scraper.config.ApiConfig;
import org.scraper.config.ApiRateLimitConfig;
import org.scraper.config.DomainConfig;
import org.scraper.config.WebScrapperConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class HttpClientService {
    private final WebScrapperConfig config;
    private final HttpClient httpClient;
    private final Map<String, Long> requestTimestamps = new ConcurrentHashMap<>();
    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();


    public HttpClientService(WebScrapperConfig config,
                             HttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
    }

    public FetchResult fetchUrl(String url) throws Exception {
        // Extract the API endpoint from the URL
        String apiEndpoint = DomainUtils.extractEndpoint(url);
        String domain = DomainUtils.extractDomain(url);

        // Get the domain configuration
        DomainConfig domainConfig = config.getDomainConfigOrDefault(domain);

        // Get the API configuration
        ApiConfig apiConfig = domainConfig.getApiConfig().get(apiEndpoint);

        // Get the appropriate rate limit configuration
        ApiRateLimitConfig rateLimitConfig;
        if (apiConfig != null) {
            rateLimitConfig = apiConfig.getRateLimitConfig();
        } else {
            rateLimitConfig = domainConfig.getDefaultRateLimitConfig(); // Use default domain rate limit if API not found
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Apply rate limiting based on the configuration
        applyRateLimit(domain, apiEndpoint, rateLimitConfig);

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String contentType = response.headers().firstValue("Content-Type").orElse("text/html").split(";")[0];
        return FetchResult.builder()
                .success(true)
                .message(response.body())
                .contentType(contentType)
                .build();
    }

    private void applyRateLimit(String domain, String apiEndpoint, ApiRateLimitConfig rateLimitConfig) {
        String key = domain + apiEndpoint;
        long currentTime = System.currentTimeMillis();
        long timeWindowMillis = TimeUnit.SECONDS.toMillis(rateLimitConfig.getTimeWindowInSeconds());

        synchronized (this) {
            requestTimestamps.putIfAbsent(key, currentTime);
            requestCounts.putIfAbsent(key, 0);

            long elapsedTime = currentTime - requestTimestamps.get(key);

            if (elapsedTime > timeWindowMillis) {
                // Reset the count and timestamp if the time window has passed
                requestCounts.put(key, 1);
                requestTimestamps.put(key, currentTime);
            } else {
                int currentCount = requestCounts.get(key);
                if (currentCount >= rateLimitConfig.getRateLimit()) {
                    // If the rate limit is exceeded, wait for the time window to reset
                    long waitTime = timeWindowMillis - elapsedTime;
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    // Reset the count and timestamp after waiting
                    requestCounts.put(key, 1);
                    requestTimestamps.put(key, System.currentTimeMillis());
                } else {
                    // Increment the count if the rate limit is not exceeded
                    requestCounts.put(key, currentCount + 1);
                }
            }
        }
    }
}