package org.scraper.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DomainConfig {
    private Map<String, ApiConfig> apiConfig = new HashMap<>(); // API configurations for the domain
    private ApiRateLimitConfig defaultRateLimitConfig; // Default rate limit for the domain
}
