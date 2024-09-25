package org.scraper.factory.handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLUrlResponseHandler implements URLResponseHandler {
    @Override
    public String handle(String responseBody) {
        Document doc = Jsoup.parse(responseBody);
        return doc.select("h1.product-title").text();
    }
}