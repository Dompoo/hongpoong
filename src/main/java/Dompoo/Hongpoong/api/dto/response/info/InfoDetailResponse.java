package Dompoo.Hongpoong.api.dto.response.info;

import Dompoo.Hongpoong.domain.Info;
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

    public InfoDetailResponse(Info info) {
        this.id = info.getId();
        this.title = info.getTitle();
        this.content = info.getContent();
        this.date = info.getDate();
    }
}
