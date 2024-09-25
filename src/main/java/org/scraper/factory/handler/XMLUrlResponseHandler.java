package org.scraper.factory.handler;

import org.scraper.DomainUtils;

public class XMLUrlResponseHandler implements URLResponseHandler {
    @Override
    public String handle(String response, String url) {
        String domain = DomainUtils.extractDomain(url);
        System.out.println("Handling XML Response: " + response);
        return null;
    }
}

