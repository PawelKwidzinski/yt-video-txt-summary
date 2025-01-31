package pl.kwidz.ytvideotxtsummary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
class YtSubDownloaderService {

    private final OllamaChatModel chatModel;
    private final YTSubConverter ytSubConverter;

    String getSummaryFromYtTxt(String ytId, String language, String prompt) throws IOException, InterruptedException {

        String fileName = String.format("%s.%s.vtt", ytId, language);

        String projectPath = new File(".").getAbsolutePath();

        Process process = Runtime.getRuntime().exec(String.format(
                "docker run --rm -v %s:/app jauderho/yt-dlp " +
                        "--write-auto-sub --sub-lang %s --skip-download " +
                        "-P /app/subtitles " +  // Keep Docker path consistent
                        "-o %s " +
                        "https://www.youtube.com/watch?v=%s", projectPath, language, ytId, ytId
        ));

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            log.info(line);
        }
        while ((line = errorReader.readLine()) != null) {
            log.info(line);
        }
        process.waitFor();
        log.info("Finish downloading");

        // cleaning and conversion to txt without yt timestamps
        log.info("Starting cleaning file: {}", fileName);
        ytSubConverter.cleanTranscript(fileName);

        FileSystemResource subtitles = new FileSystemResource(String.format("clean_subtitles/%s", fileName));
        String subtitlesContent = StreamUtils.copyToString(subtitles.getInputStream(), StandardCharsets.UTF_8);

        log.info("Prompt processing: {}", prompt);
        ChatResponse response = chatModel.call(
                new Prompt(prompt + "\n" + subtitlesContent));

        return response.getResult().getOutput().getText();
    }

}
