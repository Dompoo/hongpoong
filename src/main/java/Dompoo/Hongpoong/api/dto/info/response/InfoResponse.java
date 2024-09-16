package Dompoo.Hongpoong.api.dto.info.response;

import Dompoo.Hongpoong.domain.jpaEntity.InfoJpaEntity;
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
    private final Long infoId;
    
    @Schema(example = "걸궁 답사")
    private final String title;
    
    @Schema(example = "2023-09-09T12:34:56")
    private final LocalDateTime date;
    
    public static InfoResponse from(InfoJpaEntity infoJpaEntity) {
        return InfoResponse.builder()
                .infoId(infoJpaEntity.getId())
                .title(infoJpaEntity.getTitle())
                .date(infoJpaEntity.getDate())
                .build();
    }
}
