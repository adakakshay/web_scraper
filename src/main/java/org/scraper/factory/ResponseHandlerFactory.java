package org.scraper.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.scraper.config.ConfigurationLoader;
import org.scraper.config.WebScrapperConfig;
import org.scraper.factory.handler.HTMLUrlResponseHandler;
import org.scraper.factory.handler.JSONUrlResponseHandler;
import org.scraper.factory.handler.URLResponseHandler;
import org.scraper.factory.handler.XMLUrlResponseHandler;

public class ResponseHandlerFactory {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ConfigurationLoader config = ConfigurationLoader.getInstance();
    public static URLResponseHandler getHandler(String url) {
        if (url.endsWith(".json")) {
            return new JSONUrlResponseHandler(objectMapper, config.getConfig());
        } else if (url.endsWith(".html")) {
            return new HTMLUrlResponseHandler();
        } else if (url.endsWith(".xml")) {
            return new XMLUrlResponseHandler();
        }
        throw new IllegalArgumentException("No handler for the given URL type.");
    }
}
