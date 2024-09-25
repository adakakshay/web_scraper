package org.scraper.factory;

import org.scraper.factory.fetcher.*;

public class UrlFetcherFactory {

    public static UrlFetcher getFetcher(String url) {
        if (url.endsWith(".json")) {
            return new JsonUrlFetcher();
        } else if (url.endsWith(".html")) {
            return new HtmlUrlFetcher();
        } else if (url.endsWith(".xml")) {
            return new XmlUrlFetcher();
        } else {
            return new DefaultUrlFetcher();
        }
    }
}
