package org.scraper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigurationLoader {
    private static ConfigurationLoader instance;
    private WebScrapperConfig config;

    private ConfigurationLoader() throws IOException {
        loadConfigurations();
    }

    // Singleton Pattern
    public static ConfigurationLoader getInstance() throws IOException {
        if (instance == null) {
            synchronized (ConfigurationLoader.class) {
                if (instance == null) {
                    instance = new ConfigurationLoader();
                }
            }
        }
        return instance;
    }

    private void loadConfigurations() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        this.config = mapper.readValue(Files.readAllBytes(Paths.get("src/main/resources/config.yml")), WebScrapperConfig.class);
    }

    public WebScrapperConfig getConfig() {
        return config;
    }
}