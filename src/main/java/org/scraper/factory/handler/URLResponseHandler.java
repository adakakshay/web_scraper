package org.scraper.factory.handler;

import java.util.Map;

public interface URLResponseHandler {
    Map<String, String> handle(String response, String url);
}
