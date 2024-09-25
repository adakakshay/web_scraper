package org.scraper.observer;

public class LoggingObserver implements UrlProcessingObserver {
    @Override
    public void update(String url, String result) {
        System.out.println("Logged URL: " + url + " Result: " + result);
    }
}