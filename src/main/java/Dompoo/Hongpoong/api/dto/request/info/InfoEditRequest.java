package Dompoo.Hongpoong.api.dto.request.info;

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
    "title":"새로운 공지사항 제목",
    "content":"새로운 공지사항 내용"
}
 */
public class InfoEditRequest {

    private String title;
    private String content;

    @Builder
    private InfoEditRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    public InfoEditDto toDto() {
        return InfoEditDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}
