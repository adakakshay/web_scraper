package org.scraper.config;

import lombok.Data;

@Data
public class ApiRateLimitConfig {
    private int rateLimit;
    private int timeWindowInSeconds;
}