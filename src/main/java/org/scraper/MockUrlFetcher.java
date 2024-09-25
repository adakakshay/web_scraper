package org.scraper;

public class MockUrlFetcher {
    public static String fetch(String url) {
        if (url.endsWith(".json")) {
            return "{\"title\":\"My Title\"}";
        } else if (url.endsWith(".html")) {
            return "<html> <head> </head> Seniar BE Dev Thank you, will do. Thanky Reply Forward <body> <class=\"product-title\" data-id=\"f3bfa24c-2645-48c8-9117-b338bef9b9ab\">Product title</h1>";
        } else if (url.endsWith(".xml")) {
            return "<root><title>My XML Title</title></root>";
        }
        throw new IllegalArgumentException("No mock response for the given URL type.");
    }
}