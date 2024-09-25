package org.scraper.factory.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.scraper.DomainUtils;
import org.scraper.config.WebScrapperConfig;

import java.io.IOException;
import java.util.List;

public class JSONUrlResponseHandler implements URLResponseHandler {
    private final ObjectMapper objectMapper;
    private final WebScrapperConfig config;

    public JSONUrlResponseHandler(ObjectMapper objectMapper, WebScrapperConfig config) {
        this.objectMapper = objectMapper;
        this.config = config;
    }

    @Override
    public String handle(String responseBody, String url) {
        try {
            String domain = DomainUtils.extractDomain(url);
            String path = DomainUtils.extractEndpoint(url);
            List<String> tags = List.of(config.getDomainConfig().get(domain).getApiConfig().get(path).getTags());

            JsonNode jsonNode = objectMapper.readTree(responseBody);
            for (String tag : tags) {
                JsonNode tagNode = jsonNode.get(tag);
                if (tagNode != null) {
                    return tagNode.asText();
                }
            }
            System.err.println("No matching tags found in the JSON response for URL: " + url);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}