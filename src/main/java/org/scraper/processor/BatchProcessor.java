package org.scraper.processor;


import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BatchProcessor {
    private final BlockingQueue<String> urlQueue;
    private final int batchSize;

    public BatchProcessor(int queueSize, int batchSize) {
        this.urlQueue = new LinkedBlockingQueue<>(queueSize);
        this.batchSize = batchSize;
    }

    public void publishUrls(List<String> urls) {
        for (int i = 0; i < urls.size(); i += batchSize) {
            int end = Math.min(i + batchSize, urls.size());
            List<String> batch = urls.subList(i, end);

            for (String url : batch) {
                try {
                    urlQueue.put(url);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public BlockingQueue<String> getUrlQueue() {
        return urlQueue;
    }
}


