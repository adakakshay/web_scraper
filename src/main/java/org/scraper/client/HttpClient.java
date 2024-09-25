package org.scraper.client;

import org.scraper.DomainUtils;
import org.scraper.FetchResult;
import org.scraper.config.ApiConfig;
import org.scraper.config.ApiRateLimitConfig;
import org.scraper.config.DomainConfig;
import org.scraper.config.RateLimitConfig;
import org.scraper.config.WebScrapperConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    private final WebScrapperConfig config;
    private final Map<String, RateLimitConfig> rateLimiters = new HashMap<>();  // Store rate limiters per API

    public HttpClient(WebScrapperConfig config) {
        this.config = config;
    }

    public FetchResult fetchUrl(String url) throws Exception {
        // Extract the API endpoint from the URL
        String apiEndpoint = DomainUtils.extractEndpoint(url);
        String domain = DomainUtils.extractDomain(url);

        // Get the domain configuration
        DomainConfig domainConfig = config.getDomainConfig().getOrDefault(domain, config.getDefaultDomainConfig());

        // Get the API configuration
        ApiConfig apiConfig = domainConfig.getApiConfig().get(apiEndpoint);

        // Get the appropriate rate limit configuration
        ApiRateLimitConfig rateLimitConfig;
        if (apiConfig != null) {
            rateLimitConfig = apiConfig.getRateLimitConfig();
        } else {
            rateLimitConfig = domainConfig.getDefaultRateLimitConfig(); // Use default domain rate limit if API not found
        }

        // Apply rate limiting based on the configuration
        applyRateLimit(domain, apiEndpoint, rateLimitConfig);

        // Make the request
        return fetch(url);
    }

    private void applyRateLimit(String domain, String apiEndpoint, ApiRateLimitConfig rateLimitConfig) {
        // Create a unique key for the domain and API endpoint
        String key = domain + apiEndpoint;

        // Get or create a RateLimiter for this specific domain/API
        RateLimitConfig rateLimiter = rateLimiters.computeIfAbsent(key, k -> new RateLimitConfig());
        rateLimiter.applyRateLimit(rateLimitConfig);
    }

    public FetchResult fetch(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new FetchResult(true, response.toString());
        } else {
            throw new Exception("Failed to fetch URL: " + responseCode);
        }
    }
}
