package org.scraper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class GenericFileLoader {
    //This is to load the urls from the files.
    public List<String> loadTextFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.lines(path)
                .map(String::trim)  // Trim each line
                .filter(line -> !line.isEmpty()) // Filter out empty lines
                .collect(Collectors.toList()); // Collect into a list
    }
}
