package Dompoo.Hongpoong.api.dto.info.response;

import Dompoo.Hongpoong.domain.entity.Info;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InfoResponse {

    private final Long id;
    private final String title;
    private final LocalDateTime date;
    
    public static InfoResponse from(Info info) {
        return InfoResponse.builder()
                .id(info.getId())
                .title(info.getTitle())
                .date(info.getDate())
                .build();
    }
}
