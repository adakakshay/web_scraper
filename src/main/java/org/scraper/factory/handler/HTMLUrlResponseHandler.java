package org.scraper.factory.handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.scraper.DomainUtils;

public class HTMLUrlResponseHandler implements URLResponseHandler {
    @Override
    public String handle(String responseBody, String url) {
        String domain = DomainUtils.extractDomain(url);
        Document doc = Jsoup.parse(responseBody);
        return doc.select("h1.product-title").text();
    }
}