package org.scraper.observer;

public interface UrlProcessingObserver {
    void update(String url, String result);
}
