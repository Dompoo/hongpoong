package Dompoo.Hongpoong.api.dto.Instrument.response;

import Dompoo.Hongpoong.domain.jpaEntity.InstrumentJpaEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentResponse {
    
    @Schema(example = "1")
    private final Long instrumentId;
    
    @Schema(example = "검정 장구 8.5치")
    private final String name;
    
    @Schema(example = "장구")
    private final String type;
    
    @Schema(example = "산틀")
    private final String club;
    
    @Schema(example = "false")
    private final Boolean available;
    
    public static InstrumentResponse from(InstrumentJpaEntity instrumentJpaEntity) {
        return InstrumentResponse.builder()
                .instrumentId(instrumentJpaEntity.getId())
                .name(instrumentJpaEntity.getName())
                .type(instrumentJpaEntity.getType().korName)
                .club(instrumentJpaEntity.getClub().korName)
                .available(instrumentJpaEntity.getAvailable())
                .build();
    }
}
