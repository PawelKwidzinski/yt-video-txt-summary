package pl.kwidz.ytvideotxtsummary;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
class YTSubConverter {

    void cleanTranscript(String fileName) throws IOException {

        StringBuilder result = new StringBuilder();
        String lastCleanedLine = null; // stores the last line added
        boolean isTextLine = false;

        Path inputPath = Paths.get("subtitles", fileName);
        log.info("Input path: {}", inputPath.toAbsolutePath());

        Path outputPath = Paths.get("subtitles-clean", fileName);
        log.info("Output path: {}", outputPath.toAbsolutePath());

        // Create directories if they do not exist
        Files.createDirectories(inputPath.getParent());
        Files.createDirectories(outputPath.getParent());

        try (BufferedReader br = Files.newBufferedReader(inputPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                // We omit headers and blank lines
                if (line.startsWith("WEBVTT") || line.startsWith("Kind:") || line.startsWith("Language:") || line.trim().isEmpty()) {
                    continue;
                }
                // Line with timestamp - next line will be text
                if (line.contains("-->")) {
                    isTextLine = true;
                    continue;
                }
                if (isTextLine) {
                    // We remove HTML tags and extra spaces
                    String cleanedLine = line.replaceAll("<[^>]+>", "")
                            .replaceAll("\\s+", " ")
                            .trim();
                    // Add a line only if it is not empty and does not repeat the previous one
                    if (!cleanedLine.isEmpty() && !cleanedLine.equals(lastCleanedLine)) {
                        if (!result.isEmpty()) {
                            result.append("\n");
                        }
                        result.append(cleanedLine);
                        lastCleanedLine = cleanedLine;
                    }
                    isTextLine = false;
                }
            }
        } catch (IOException e) {
            log.error("Error during cleaning file {}", e.toString());
        }

        saveToFile(outputPath.toString(), result.toString());
    }

    private static void saveToFile(String filePath, String content) {
        Path path = Paths.get(filePath);
        try {
            // We make sure that the directory exists
            Files.createDirectories(path.getParent());
            // Save file
            Files.writeString(path, content);
            log.info("Successfully saved to: {}", path.toAbsolutePath());
        } catch (IOException e) {
            log.error("Error saving file {}: {}", path, e.getMessage());
            throw new RuntimeException("Unable to save file: " + path, e);
        }
    }
}

