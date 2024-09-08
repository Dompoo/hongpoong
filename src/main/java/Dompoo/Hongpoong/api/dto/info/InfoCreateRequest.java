package Dompoo.Hongpoong.api.dto.info;

import Dompoo.Hongpoong.domain.entity.Info;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private InfoCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    public Info toInfo(LocalDateTime now) {
        return Info.builder()
                .title(title)
                .content(content)
                .date(now)
                .build();
    }
    
    
}
