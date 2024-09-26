package org.scraper.factory.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.scraper.DomainUtils;
import org.scraper.config.ApiConfig;
import org.scraper.config.WebScrapperConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUrlResponseHandler implements URLResponseHandler {
    private final ObjectMapper objectMapper;
    private final WebScrapperConfig config;

    public JSONUrlResponseHandler(ObjectMapper objectMapper, WebScrapperConfig config) {
        this.objectMapper = objectMapper;
        this.config = config;
    }

    @Override
    public Map<String, String> handle(String responseBody, String url) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            String domain = DomainUtils.extractDomain(url);
            String path = DomainUtils.extractEndpoint(url);
            Map<String, ApiConfig> apiConfigMap = config.getDomainConfig().get(domain).getApiConfig();

            List<String> tags = null;
            for (String key : apiConfigMap.keySet()) {
                if (path.contains(key)) {
                    tags = List.of(apiConfigMap.get(key).getTags());
                    break;
                }
            }

            if (tags == null) {
                System.err.println("No matching tags found in the configuration for URL: " + url);
                System.out.println("Response body: " + responseBody);
                return resultMap;
            }

            JsonNode jsonNode = objectMapper.readTree(responseBody);
            for (String tag : tags) {
                JsonNode tagNode = jsonNode.get(tag);
                if (tagNode != null) {
                    resultMap.put(tag, tagNode.asText());
                }
            }

            if (resultMap.isEmpty()) {
                System.err.println("No matching tags found in the JSON response for URL: " + url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}