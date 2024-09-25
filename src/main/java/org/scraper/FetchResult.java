package org.scraper;

import lombok.Data;

@Data
public class FetchResult {
    private final boolean success;
    private final String message;
    private final String contentType;

    public FetchResult(boolean success, String message, String contentType) {
        this.success = success;
        this.message = message;
        this.contentType = contentType;
    }
}
