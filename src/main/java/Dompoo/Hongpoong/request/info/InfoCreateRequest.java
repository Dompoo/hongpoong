package Dompoo.Hongpoong.request.info;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class InfoCreateRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    @Builder
    public InfoCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
