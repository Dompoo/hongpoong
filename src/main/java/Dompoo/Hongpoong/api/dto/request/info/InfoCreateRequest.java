package Dompoo.Hongpoong.api.dto.request.info;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
/*
RequestBody
{
    "title":"공지사항 제목",
    "content":"공지사항 내용"
}
 */
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
