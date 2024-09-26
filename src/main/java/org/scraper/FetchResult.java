package org.scraper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
