package org.scraper.config;

import lombok.Data;

import java.util.Map;

@Data
public class WebScrapperConfig {
    private DomainConfig defaultDomainConfig;
    private Map<String, DomainConfig> domainConfig;
    private BatchProcessingConfig batchProcessingConfig;

    public DomainConfig getDomainConfigOrDefault(String domain) {
        return domainConfig != null ? domainConfig.getOrDefault(domain, defaultDomainConfig) : defaultDomainConfig;
    }
}
