package Dompoo.Hongpoong.domain.domain;

import Dompoo.Hongpoong.api.dto.info.request.InfoEditDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Info {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime date;
    private Member member;
    
    public void edit(InfoEditDto dto) {
        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getContent() != null) this.content = dto.getContent();
    }
}
