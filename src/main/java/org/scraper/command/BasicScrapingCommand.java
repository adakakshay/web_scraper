package org.scraper.command;

import org.scraper.client.HttpClient;
import org.scraper.factory.ResponseHandlerFactory;
import org.scraper.factory.handler.URLResponseHandler;

import javax.inject.Inject;

public class BasicScrapingCommand implements ScrappingCommand {
    private final HttpClient httpClient;

    public BasicScrapingCommand(){
        this.httpClient = new HttpClient();
    }
    @Override
    public void execute(String url) {
        try {
            String responseBody = httpClient.fetch(url);
            URLResponseHandler handler = ResponseHandlerFactory.getHandler(url);
            String result = handler.handle(responseBody);
            System.out.println("Processed result: " + result);
        } catch (Exception e) {
            System.err.println("Failed to process URL: " + e.getMessage());
        }
    }
}