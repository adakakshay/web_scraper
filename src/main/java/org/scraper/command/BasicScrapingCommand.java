package org.scraper.command;

import org.scraper.FetchResult;
import org.scraper.client.HttpClient;
import org.scraper.factory.ResponseHandlerFactory;
import org.scraper.factory.handler.URLResponseHandler;

public class BasicScrapingCommand implements ScrappingCommand {
    private final HttpClient httpClient;

    public BasicScrapingCommand(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void execute(String url)  {
        try {
            FetchResult fetchResult = httpClient.fetchUrl(url);
            if (fetchResult.isSuccess()) {
                URLResponseHandler handler = ResponseHandlerFactory.getHandler(fetchResult.getContentType());
                String result = handler.handle(fetchResult.getMessage(), url);
                System.out.println("Processed result: " + result);
            }
        } catch (Exception e) {
            System.err.println("Failed to process URL: " + e.getMessage());
//            throw new RuntimeException(e); this can be used for retry logic
        }
    }
}
