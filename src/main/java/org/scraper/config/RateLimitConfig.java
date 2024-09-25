package org.scraper.config;

import com.google.common.util.concurrent.RateLimiter;
import lombok.Data;
@Data
public class RateLimitConfig {
    private RateLimiter rateLimiter;

    public void applyRateLimit(ApiRateLimitConfig config) {
        if (config != null) {
            rateLimiter = RateLimiter.create(config.getRateLimit());
            rateLimiter.acquire(); // Acquire the rate limit permit before making the request
        }
    }
}