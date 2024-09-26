package org.scraper.command;

import org.scraper.FetchResult;
import org.scraper.client.HttpClientService;
import org.scraper.factory.ResponseHandlerFactory;
import org.scraper.factory.handler.URLResponseHandler;

import java.util.Map;

public class BasicScrapingCommand implements ScrappingCommand {
    private final HttpClientService httpClientService;

    public BasicScrapingCommand(HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    @Override
    public void execute(String url)  {
        try {
            FetchResult fetchResult = httpClientService.fetchUrl(url);
            if (fetchResult.isSuccess()) {
                URLResponseHandler handler = ResponseHandlerFactory.getHandler(fetchResult.getContentType());
                Map<String, String> result = handler.handle(fetchResult.getMessage(), url);
                System.out.println("Processed result for url " + url + " with Tags: " + result);
            }
        } catch (Exception e) {
            System.err.println("Failed to process URL: " + e.getMessage());
//            throw new RuntimeException(e); this can be used for retry logic
        }
    }
}
