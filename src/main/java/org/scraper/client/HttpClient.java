package org.scraper.client;

import org.scraper.DomainUtils;
import org.scraper.FetchResult;
import org.scraper.config.ApiConfig;
import org.scraper.config.ApiRateLimitConfig;
import org.scraper.config.DomainConfig;
import org.scraper.config.WebScrapperConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private final WebScrapperConfig config;

    public HttpClient(WebScrapperConfig config) {
        this.config = config;
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

        // Apply rate limiting based on the configuration
        applyRateLimit(domain, apiEndpoint, rateLimitConfig);

        // Make the request
        return fetch(url);
    }

    private void applyRateLimit(String domain, String apiEndpoint, ApiRateLimitConfig rateLimitConfig) {
        // Implementation of rate limiting
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