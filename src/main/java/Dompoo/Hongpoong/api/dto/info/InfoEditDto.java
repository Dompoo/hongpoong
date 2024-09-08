package Dompoo.Hongpoong.api.dto.info;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InfoEditDto {

    private String title;
    private String content;

    @Builder
    private InfoEditDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
