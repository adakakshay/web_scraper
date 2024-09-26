package org.scraper.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.scraper.config.ConfigurationLoader;
import org.scraper.factory.handler.HTMLUrlResponseHandler;
import org.scraper.factory.handler.JSONUrlResponseHandler;
import org.scraper.factory.handler.URLResponseHandler;
import org.scraper.factory.handler.XMLUrlResponseHandler;

public class ResponseHandlerFactory {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ConfigurationLoader config = ConfigurationLoader.getInstance();
    public static URLResponseHandler getHandler(String contentType) {
        switch (contentType.toLowerCase()) {
            case "application/json":
                return new JSONUrlResponseHandler(objectMapper, config.getConfig());
            case "text/html":
                return new HTMLUrlResponseHandler();
            case "application/xml":
                return new XMLUrlResponseHandler();
            default:
                throw new IllegalArgumentException("No handler for the given content type: " + contentType);
        }
    }
}
