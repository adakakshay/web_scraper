package org.scraper.observer;

import java.util.ArrayList;
import java.util.List;

public class UrlProcessingNotifier {
    private final List<UrlProcessingObserver> observers = new ArrayList<>();

    public void addObserver(UrlProcessingObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(UrlProcessingObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String url, String result) {
        for (UrlProcessingObserver observer : observers) {
            observer.update(url, result);
        }
    }
}



