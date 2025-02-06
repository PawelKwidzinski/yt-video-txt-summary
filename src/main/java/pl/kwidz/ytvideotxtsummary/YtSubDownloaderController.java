package pl.kwidz.ytvideotxtsummary;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
class YtSubDownloaderController {

    private final YtSubDownloaderService service;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("ytSubtitle", new YtSubtitle());
        return "index";
    }

    @PostMapping("/save")
    public String handleFormSubmission(@Valid YtSubtitle ytSubtitle, Model model) throws Exception {
        log.info("Saving ytSubtitle: {}", ytSubtitle);
        model.addAttribute("ytSubtitle", ytSubtitle);
        String summary = service.getSummaryFromYtTxt(ytSubtitle.getYtId(), ytSubtitle.getLanguage(), ytSubtitle.getPrompt());
        model.addAttribute("summary", summary);
        return "index";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        YtSubtitle ytSubtitle = (YtSubtitle) ex.getBindingResult().getTarget();
        model.addAttribute("ytSubtitle", ytSubtitle);
        model.addAttribute("error", "Please fill out all required fields.");
        log.info("ytSubtitle Validation failed for: {}", ytSubtitle);
        return "index";
    }

}
