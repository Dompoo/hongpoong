package Dompoo.Hongpoong.api.dto.response.info;

import Dompoo.Hongpoong.domain.entity.Info;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
/*
ResponseBody
<단건조회시>
{
    "id": 1,
    "title": "공지사항 제목",
    "content": "공지사항 내용",
    "date": "2024-05-01T14:49:58.836833"
}
 */
public class InfoDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime date;
    
    @Builder
    private InfoDetailResponse(Long id, String title, String content, LocalDateTime date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }
    
    @Builder
    public static InfoDetailResponse from (Info info) {
        return InfoDetailResponse.builder()
                .id(info.getId())
                .title(info.getTitle())
                .content(info.getContent())
                .date(info.getDate())
                .build();
    }
}
