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
public class InfoDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime date;
    
    public static InfoDetailResponse from (Info info) {
        return InfoDetailResponse.builder()
                .id(info.getId())
                .title(info.getTitle())
                .content(info.getContent())
                .date(info.getDate())
                .build();
    }
}
