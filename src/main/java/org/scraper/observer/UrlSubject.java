package org.scraper.observer;

public interface UrlSubject {
    void addObserver(UrlObserver observer);
    void notifyObservers();
}
