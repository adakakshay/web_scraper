package org.scraper;

import lombok.Data;

@Data
public class FetchResult {
    private final boolean success;
    private final String message;

    public FetchResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
