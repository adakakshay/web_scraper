package org.scraper.factory.handler;

import org.scraper.DomainUtils;

import java.util.HashMap;
import java.util.Map;

public class XMLUrlResponseHandler implements URLResponseHandler {
    @Override
    public Map<String, String> handle(String response, String url) {
        System.out.println("Handling XML Response: " + response);
        return new HashMap<>();
    }
}

