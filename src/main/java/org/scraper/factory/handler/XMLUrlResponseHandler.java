package org.scraper.factory.handler;

public class XMLUrlResponseHandler implements URLResponseHandler {
    @Override
    public String handle(String response) {
        // Handle XML response
        System.out.println("Handling XML Response: " + response);
        return null;
    }
}

