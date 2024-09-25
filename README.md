Web Scraper Project
Overview

This web scraper is designed to handle scraping tasks efficiently and in a structured manner. It uses a batch processing approach to scrape multiple URLs, with rate-limiting, multi-threading, and customizable configurations for different APIs.

The main components of the system include:

    Configuration Management: Handles domain-specific configurations and rate limits.
    URL Processing: Reads URLs from files and processes them in batches.
    Rate Limiting: Applies API rate limits dynamically based on configuration.
    Response Handling: Handles different types of responses like JSON, HTML, and XML.
    Multi-threading: Uses a thread pool to process URLs in parallel.

Features

    Batch Processing: The system can process URLs in batches for efficient scraping.
    Multi-threaded Execution: Multiple consumers can scrape URLs in parallel, improving performance.
    Rate Limiting: Rate limits can be set per API endpoint, allowing controlled and respectful scraping.
    Flexible Configuration: Domain-specific configurations for API rate limits, content types, and other parameters are supported using YAML configuration files.
    Response Handlers: The scraper supports JSON, HTML, and XML responses with appropriate handlers for each content type.

Project Structure



        src
        ├── main
        │   ├── java
        │   │   ├── org
        │   │   │   ├── scraper
        │   │   │   │   ├── command           # Contains scrapping commands for executing URL scrapes
        │   │   │   │   ├── config            # Configuration classes (e.g., rate limit, domain, batch processing)
        │   │   │   │   ├── client            # HttpClient to fetch URL data
        │   │   │   │   ├── factory           # ResponseHandlerFactory and its handlers (JSON, HTML, XML)
        │   │   │   │   ├── processor         # Components responsible for file reading, batching, and consuming URLs
        │   │   │   │   ├── FetchResult.java  # Holds the result of URL fetching
        │   │   │   │   └── WebScraper.java   # Main entry point for the scraper application
        │   └── resources
        │       └── config.yml                # YAML file containing configuration for the scraper

Configuration

The configuration is managed using a YAML file (config.yml). It defines domain-specific API configurations, rate limits, and batch processing options.
Example Configuration (config.yml):

        defaultDomainConfig:
          defaultRateLimitConfig:
            rateLimit: 10
            timeWindowInSeconds: 60
        domainConfig:
          "example.com":
            apiConfig:
              "/api/v1/products":
                rateLimitConfig:
                  rateLimit: 5
                  timeWindowInSeconds: 30
                contentType: "json"
                tags: ["data", "items"]
        batchProcessingConfig:
          batchSize: 100
          threadPoolSize: 10

Key Configuration Components:

    Domain Configurations: Domain-specific rate limits and API endpoint configurations. The default configuration applies when a domain or endpoint is not explicitly defined.
    Rate Limits: Specifies how many requests can be made in a given time window.
    Batch Processing: Defines batch size and thread pool size for the scraping process.

Usage
Prerequisites

    Java 8+
    Maven (for building the project)

Steps to Run the Web Scraper
Clone the repository:

      git clone <repository-url>
      cd web-scraper

Configure the scraper: Update the config.yml file in the src/main/resources folder with your desired API configurations, rate limits, and batch sizes.

Prepare the URLs file: Place the URLs you wish to scrape in a text file named urls.txt under the src/main/resources/ directory. Each line should contain a single URL.

Build the project:

    mvn clean install

Run the scraper:

    java -jar target/web-scraper.jar
