package Dompoo.Hongpoong.api.dto.Instrument.response;

import Dompoo.Hongpoong.domain.jpaEntity.InstrumentJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.InstrumentBorrowJpaEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentDetailResponse {
    
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
    
    private final List<InstrumentBorrowResponse> borrowHistory;
    
    public static InstrumentDetailResponse from(InstrumentJpaEntity instrumentJpaEntity, List<InstrumentBorrowJpaEntity> instrumentBorrowJpaEntities) {
        return InstrumentDetailResponse.builder()
                .instrumentId(instrumentJpaEntity.getId())
                .name(instrumentJpaEntity.getName())
                .type(instrumentJpaEntity.getType().korName)
                .club(instrumentJpaEntity.getClub().korName)
                .available(instrumentJpaEntity.getAvailable())
                .borrowHistory(InstrumentBorrowResponse.fromList(instrumentBorrowJpaEntities))
                .build();
    }
}
