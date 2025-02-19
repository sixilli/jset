package org.jset.services;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@ApplicationScoped
@RegisterForReflection
public class FileService {

    private static final String BASE_DIR = "data";

    public void writeToFile(String filename, String content) throws IOException {
        // Ensure directory exists
        Files.createDirectories(Path.of(BASE_DIR));

        // Create full path
        Path filePath = Path.of(BASE_DIR, filename);

        // Write content
        Files.writeString(
                filePath,
                content,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    public String readFromFile(String filename) throws IOException {
        Path filePath = Path.of(BASE_DIR, filename);
        return Files.readString(filePath);
    }

    public List<String> listFiles() throws IOException {
        Path dirPath = Path.of(BASE_DIR);

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        return Files.list(dirPath)
                .map(path -> path.getFileName().toString())
                .toList();
    }
}
