package Dompoo.Hongpoong.api.dto.response.info;

import Dompoo.Hongpoong.domain.entity.Info;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
/*
ResponseBody

<List조회시>
[
    {
        "id": 1,
        "title": "공지사항 제목",
        "date": "2024-05-01T14:49:58.836833"
    },
    {
        "id": 2,
        "title": "공지사항 제목2",
        "date": "2024-05-01T14:51:26.135512"
    },
    . . .
]
 */
public class InfoResponse {

    private final Long id;
    private final String title;
    private final LocalDateTime date;
    
    @Builder
    private InfoResponse(Long id, String title, LocalDateTime date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }
    
    public static InfoResponse from(Info info) {
        return InfoResponse.builder()
                .id(info.getId())
                .title(info.getTitle())
                .date(info.getDate())
                .build();
    }
}
