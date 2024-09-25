package org.scraper.config;

import java.util.Map;

public class RateLimitConfig {
    private Map<String, Integer> rateLimits;
    private int defaultRateLimit = 100; // Default to 1 request per second

    public Map<String, Integer> getRateLimits() {
        return rateLimits;
    }

    public void setRateLimits(Map<String, Integer> rateLimits) {
        this.rateLimits = rateLimits;
    }

    public int getDefaultRateLimit() {
        return defaultRateLimit;
    }

    public void setDefaultRateLimit(int defaultRateLimit) {
        this.defaultRateLimit = defaultRateLimit;
    }
}

