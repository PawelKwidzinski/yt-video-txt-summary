package pl.kwidz.ytvideotxtsummary;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
class YTSubConverter {

    void cleanTranscript(String fileName) {

        StringBuilder result = new StringBuilder();
        boolean isTextLine = false;

        // Use relative paths
        Path inputPath = Paths.get("subtitles", fileName);
        Path outputPath = Paths.get("clean_subtitles", fileName);

        try (BufferedReader br = Files.newBufferedReader(inputPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                // Pomijaj nagłówki i puste linie
                if (line.startsWith("WEBVTT") || line.startsWith("Kind:") || line.startsWith("Language:") || line.trim().isEmpty()) {
                    continue;
                }

                // Linia z timestampem - następna linia będzie zawierać tekst
                if (line.contains("-->")) {
                    isTextLine = true;
                    continue;
                }

                if (isTextLine) {
                    // Usuń znaczniki czasu i formatowania
                    String cleanedLine = line.replaceAll("<[^>]+>", "")
                            .replaceAll("\\s+", " ")
                            .trim();

                    if (!cleanedLine.isEmpty()) {
                        // Dodaj spację jeśli potrzebna
                        if (!result.isEmpty() && !result.substring(result.length() - 1).equals(" ")) {
                            result.append(" ");
                        }
                        result.append(cleanedLine);
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
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(content);
            log.info("Successfully saved to: {}", path.toAbsolutePath());
        } catch (IOException e) {
            log.error("Error saving file: {}", e.getMessage());
        }
    }

}
