package Dompoo.Hongpoong.request.info;

import lombok.Builder;
import lombok.Data;

@Data
public class InfoEditRequest {

    private String title;
    private String content;

    @Builder
    public InfoEditRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
