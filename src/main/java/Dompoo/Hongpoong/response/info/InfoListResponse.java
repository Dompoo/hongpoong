package Dompoo.Hongpoong.response.info;

import Dompoo.Hongpoong.domain.Info;
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
public class InfoListResponse {

    private final Long id;
    private final String title;
    private final LocalDateTime date;

    public InfoListResponse(Info info) {
        this.id = info.getId();
        this.title = info.getTitle();
        this.date = info.getDate();
    }
}
