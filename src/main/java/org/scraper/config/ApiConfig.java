package org.scraper.config;

import lombok.Data;

@Data
public class ApiConfig {
    private ApiRateLimitConfig rateLimitConfig;
    private String[] tags;
    private String contentType;
}
