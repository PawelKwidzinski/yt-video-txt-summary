package pl.kwidz.ytvideotxtsummary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/yt-subtitles")
class YtSubDownloaderController {

    private final YtSubDownloaderService service;

    @GetMapping
    String getResumeYtSubtitles(@RequestParam String ytId, String language, String prompt) throws IOException, InterruptedException {
        return service.getSummaryFromYtTxt(ytId, language, prompt);
    }

}
