package org.scraper.limiter;

import com.google.common.util.concurrent.RateLimiter;
import org.scraper.config.RateLimitConfig;

import java.util.HashMap;
import java.util.Map;

public class DefaultRateLimitingStrategy implements RateLimitingStrategy {
    private final Map<String, RateLimiter> domainRateLimiters;
    private final RateLimiter defaultRateLimiter;

    public DefaultRateLimitingStrategy(RateLimitConfig config) {
        domainRateLimiters = new HashMap<>();
        config.getRateLimits().forEach((domain, rate) ->
                domainRateLimiters.put(domain, RateLimiter.create(rate))
        );
        this.defaultRateLimiter = RateLimiter.create(config.getDefaultRateLimit());
    }

    @Override
    public void acquire(String domain) {
        RateLimiter rateLimiter = domainRateLimiters.getOrDefault(domain, defaultRateLimiter);
        rateLimiter.acquire();
    }
}