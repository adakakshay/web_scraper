package org.scraper;

public class DomainUtils {
    public static String extractDomain(String url) {
        try {
            return new java.net.URL(url).getHost();
        } catch (java.net.MalformedURLException e) {
            return "";
        }
    }

    public static String extractEndpoint(String url) {
        try {
            String path = new java.net.URL(url).getPath();
            return path.isEmpty() ? "/" : path; // Return the endpoint, default to "/" if empty
        } catch (java.net.MalformedURLException e) {
            return "";
        }
    }
}

