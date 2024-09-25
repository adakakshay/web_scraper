package org.scraper.limiter;

public interface RateLimitingStrategy {
    void acquire(String domain);
}
