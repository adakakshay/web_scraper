package org.scraper.factory.handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

public class HTMLUrlResponseHandler implements URLResponseHandler {
    @Override
    public Map<String, String> handle(String responseBody, String url) {
        Map<String, String> resultMap = new HashMap<>();
        Document doc = Jsoup.parse(responseBody);

        Element titleElement = doc.selectFirst("h1.product-title");

        if (titleElement != null) {
            resultMap.put("Product Title", titleElement.text());
        }

        return resultMap;
    }
}