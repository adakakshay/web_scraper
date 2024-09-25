package org.scraper.factory.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.scraper.DomainUtils;

import java.io.IOException;

public class JSONUrlResponseHandler implements URLResponseHandler {
    private final ObjectMapper objectMapper;

    public JSONUrlResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String handle(String responseBody, String url) {
        try {
            String domain = DomainUtils.extractDomain(url);
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("title").asText();  // Extract JSON title
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}