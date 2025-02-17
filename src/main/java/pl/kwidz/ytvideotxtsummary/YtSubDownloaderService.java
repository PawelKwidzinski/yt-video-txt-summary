package pl.kwidz.ytvideotxtsummary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
class YtSubDownloaderService {

    private final OllamaChatModel chatModel;
    private final YTSubConverter ytSubConverter;

    String getSummaryFromYtTxt(String ytId, String language, String prompt) throws InterruptedException, IOException {
        String fileName = String.format("%s.%s.vtt", ytId, language);
        String projectPath = new File(".").getAbsolutePath();

        // Download metadata from yt sub (title and upload_date)
        String[] metadataCommand = {
                "docker", "run", "--rm",
                "-v", projectPath + ":/app",
                "jauderho/yt-dlp",
                "--skip-download",
                "--print", "%(title)s",
                "--print", "%(upload_date)s",
                "https://www.youtube.com/watch?v=" + ytId
        };

        Process metadataProcess = Runtime.getRuntime().exec(metadataCommand);

        String title = "";
        String uploadDate = "";

        BufferedReader metadataReader = new BufferedReader(new InputStreamReader(metadataProcess.getInputStream()));
        String line;
        int lineCount = 0;
        while ((line = metadataReader.readLine()) != null) {
            if (lineCount == 0) {
                title = line;
            } else if (lineCount == 1) {
                uploadDate = line;
            }
            lineCount++;
        }
        metadataProcess.waitFor();

        // Formatting date from YYYYMMDD to YYYY-MM-DD
        if (uploadDate.length() == 8) {
            uploadDate = uploadDate.substring(0, 4) + "-" +
                    uploadDate.substring(4, 6) + "-" +
                    uploadDate.substring(6, 8);
        }

        // Download yt sub
        String[] subtitlesCommand = {
                "docker", "run", "--rm",
                "-v", projectPath + ":/app",
                "jauderho/yt-dlp",
                "--write-auto-sub",
                "--sub-lang", language,
                "--skip-download",
                "-P", "/app/subtitles",
                "-o", ytId,
                "https://www.youtube.com/watch?v=" + ytId
        };

        Process subtitlesProcess = Runtime.getRuntime().exec(subtitlesCommand);

        BufferedReader subtitlesReader = new BufferedReader(new InputStreamReader(subtitlesProcess.getInputStream()));
        while ((line = subtitlesReader.readLine()) != null) {
            log.info(line);
        }
        subtitlesProcess.waitFor();

        ytSubConverter.cleanTranscript(fileName);

        Path cleanedSubtitlesPath = Paths.get("subtitles-clean", fileName);
        FileSystemResource subtitles = new FileSystemResource(cleanedSubtitlesPath.toFile());

        if (!subtitles.exists()) {
            throw new FileNotFoundException("The subtitle file was not found: " + cleanedSubtitlesPath);
        }
        String subtitlesContent = StreamUtils.copyToString(subtitles.getInputStream(), StandardCharsets.UTF_8);

        String subtitlesMetadata = String.format(
                "Title: %s.\nCreated at: %s\nTranscription:",
                title,
                uploadDate
        );

        Prompt chatPrompt = new Prompt(prompt + "\n\n" + subtitlesMetadata + "\n" + subtitlesContent);

        ChatResponse response = chatModel.call(chatPrompt);

        return response.getResult().getOutput().getText();
    }
}
