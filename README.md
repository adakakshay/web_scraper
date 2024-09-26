Web Scraper Project
Overview

This web scraper project is designed to efficiently process a large number of URLs, apply rate limiting based on domain and API configuration, and handle different response formats like JSON, HTML, and XML. It utilizes a multithreaded approach for reading URLs, consuming them, and executing scraping commands in parallel.

The project includes the following major components:

    Configuration Management: Loads configurations like rate limits, batch processing settings, and domain-specific configurations from a YAML file.
    Rate Limiting: Applied based on the domain and API rate limit configurations to avoid overloading the target websites.
    Command Pattern: Implements a ScrappingCommand interface to execute different scraping logic depending on the response content type (JSON, HTML, XML).
    Threaded Processing: Uses producers and consumers to read URLs from a file and scrape them concurrently.
    Response Handlers: Handles different content types using a handler factory, which provides specific handlers for JSON, HTML, and XML.

Features

    Multithreaded Scraping:
        Uses producers and consumers to process URLs in parallel.
        Can customize the number of consumers to scrape URLs.

    Rate Limiting:
        Configurable rate limits based on domain or API.
        Applied dynamically depending on the API and domain configuration.

    Content-Type Based Handling:
        JSON, HTML, and XML responses are processed by corresponding handlers.
        Response handlers extract specific data points based on tags or elements.

    Configuration Management:
        Loads configurations from a config.yml file, including domain-specific rate limits, batch sizes, thread pool sizes, etc.

Architecture
Core Classes

    Main: Entry point of the application.
    WebScraper: Initializes the system and starts the scraping process using multithreading.
    ConfigurationLoader: Singleton class that loads the configuration from a YAML file.
    HttpClientService: Handles HTTP requests and applies rate limiting before fetching URLs.
    ScrappingCommand: Interface that defines the contract for executing the scraping logic.
    BasicScrapingCommand: Implements the ScrappingCommand interface and processes URLs using response handlers.
    UrlConsumer: Runnable class that consumes URLs from a queue and executes the scraping logic.
    FileReaderProcessor: Runnable class that reads URLs from a file and adds them to a queue.
    ResponseHandlerFactory: Factory class that provides the appropriate response handler based on content type (JSON, HTML, XML).
    DomainUtils: Utility class to extract domain and API endpoints from URLs.

Configuration

The project loads configurations from src/main/resources/config.yml through the ConfigurationLoader class. This configuration includes:

    Domain-specific API configurations
    Rate limit configurations

Content Type Handling

    JSON: Extracts specific fields based on the tags defined in the configuration.
    HTML: Extracts data using Jsoup and parses elements based on predefined selectors.
    XML: Uses a basic handler for XML processing.

Configuration

The configuration is loaded from a YAML file. Below is a sample structure of config.yml:

yaml

    defaultDomainConfig:
        defaultRateLimitConfig:
            rateLimit: 50
            timeWindowInSeconds: 60

    domainConfig:
        google.com:
            defaultRateLimitConfig:    # Default rate limit for this domain
                rateLimit: 100
                timeWindowInSeconds: 60
            apiConfig:
                /search1:
                    rateLimitConfig:
                        rateLimit: 100        # Specific rate limit for the /search API
                        timeWindowInSeconds: 60
                    tags: ["title"]
                /map:
                    rateLimitConfig:
                        rateLimit: 100        # Specific rate limit for the /maps API
                        timeWindowInSeconds: 60
                    tags: []



Key Configuration Fields

    Rate Limits: Define how many requests are allowed within a given time window.
    Domain Configurations: API-specific rate limits and configurations are defined for each domain.
    Batch Processing: Configure thread pool size and batch sizes for URL processing.

Usage
Prerequisites

    Java 11+
    Maven (for building and running the project)

Build the Project

    mvn clean install

Run the Test cases

    mvn test

TODO

    Retry Logic: Implement retry logic for failed URLs.
    Additional Response Handlers: Expand the response handlers to support more content types.
    Enhanced Error Handling: Improve error reporting and handling for rate limit errors or connection timeouts.