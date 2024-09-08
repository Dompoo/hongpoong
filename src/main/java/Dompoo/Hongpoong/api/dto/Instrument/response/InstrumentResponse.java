package Dompoo.Hongpoong.api.dto.Instrument.response;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.enums.Club;
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
    
    @Schema(example = "장구")
    private final String type;
    
    @Schema(example = "산틀")
    private final String club;
    
    public static InstrumentResponse from(Instrument instrument, Club club) {
        return InstrumentResponse.builder()
                .instrumentId(instrument.getId())
                .type(instrument.getType().korName)
                .club(club.korName)
                .build();
    }
}
