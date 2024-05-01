package Dompoo.Hongpoong.response;

import Dompoo.Hongpoong.domain.Info;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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
