package Dompoo.Hongpoong.api.dto.info.response;

import Dompoo.Hongpoong.domain.entity.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InfoResponse {
    
    @Schema(example = "1")
    private final Long id;
    
    @Schema(example = "걸궁 답사")
    private final String title;
    
    @Schema(example = "2023-09-09T12:34:56")
    private final LocalDateTime date;
    
    public static InfoResponse from(Info info) {
        return InfoResponse.builder()
                .id(info.getId())
                .title(info.getTitle())
                .date(info.getDate())
                .build();
    }
}
