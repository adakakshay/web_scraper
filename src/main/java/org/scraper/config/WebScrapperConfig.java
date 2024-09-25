package org.scraper.config;
import java.util.Map;

public class WebScrapperConfig {
    private Map<String, DomainConfig> domainConfig;
    private BatchProcessingConfig batchProcessingConfig;

    // Getters and Setters

    public Map<String, DomainConfig> getDomainConfig() {
        return domainConfig;
    }

    public void setDomainConfig(Map<String, DomainConfig> domainConfig) {
        this.domainConfig = domainConfig;
    }

    public BatchProcessingConfig getBatchProcessingConfig() {
        return batchProcessingConfig;
    }

    public void setBatchProcessingConfig(BatchProcessingConfig batchProcessingConfig) {
        this.batchProcessingConfig = batchProcessingConfig;
    }

    public static class DomainConfig {
        private RateLimitConfig rateLimitConfig;
        private Map<String, ApiConfig> apiConfig;

        // Getters and Setters

        public RateLimitConfig getRateLimitConfig() {
            return rateLimitConfig;
        }

        public void setRateLimitConfig(RateLimitConfig rateLimitConfig) {
            this.rateLimitConfig = rateLimitConfig;
        }

        public Map<String, ApiConfig> getApiConfig() {
            return apiConfig;
        }

        public void setApiConfig(Map<String, ApiConfig> apiConfig) {
            this.apiConfig = apiConfig;
        }
    }

    public static class RateLimitConfig {
        private int rateLimit;
        private int timeWindowInSeconds;

        // Getters and Setters

        public int getRateLimit() {
            return rateLimit;
        }

        public void setRateLimit(int rateLimit) {
            this.rateLimit = rateLimit;
        }

        public int getTimeWindowInSeconds() {
            return timeWindowInSeconds;
        }

        public void setTimeWindowInSeconds(int timeWindowInSeconds) {
            this.timeWindowInSeconds = timeWindowInSeconds;
        }
    }

    public static class ApiConfig {
        private String[] tags;

        // Getters and Setters

        public String[] getTags() {
            return tags;
        }

        public void setTags(String[] tags) {
            this.tags = tags;
        }
    }

    public static class BatchProcessingConfig {
        private int batchSize;
        private int threadPoolSize;

        // Getters and Setters

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public int getThreadPoolSize() {
            return threadPoolSize;
        }

        public void setThreadPoolSize(int threadPoolSize) {
            this.threadPoolSize = threadPoolSize;
        }
    }
}