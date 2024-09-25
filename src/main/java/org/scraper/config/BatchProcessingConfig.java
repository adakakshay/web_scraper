package org.scraper.config;

import lombok.Data;

@Data
public class BatchProcessingConfig {
    private int batchSize;
    private int threadPoolSize;
}