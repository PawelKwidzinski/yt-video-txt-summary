package pl.kwidz.ytvideotxtsummary;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class YtSubtitle {
    @NotBlank(message = "Youtube Id field should be blank.")
    String ytId;
    @NotBlank(message = "Language field should be blank.")
    String language;
    @NotBlank(message = "Prompt field should be blank.")
    String prompt;
}
