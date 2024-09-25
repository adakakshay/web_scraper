package org.scraper.processor;

import org.scraper.command.ScrappingCommand;
import org.scraper.limiter.RateLimitingStrategy;

public class DomainProcessor {
    private final RateLimitingStrategy rateLimitingStrategy;
    private final ScrappingCommand scrappingCommand;

    public DomainProcessor(RateLimitingStrategy rateLimitingStrategy, ScrappingCommand scrappingCommand) {
        this.rateLimitingStrategy = rateLimitingStrategy;
        this.scrappingCommand = scrappingCommand;
    }

    public void process(String url) throws Exception {
        String domain = extractDomain(url);

        // Rate limiting - acquire permission before proceeding
        rateLimitingStrategy.acquire(domain);

        // Execute the scraping command (fetch and process the URL)
        scrappingCommand.execute(url);
    }

    private String extractDomain(String url) {
        try {
            return new java.net.URL(url).getHost();
        } catch (java.net.MalformedURLException e) {
            return "";
        }
    }
}