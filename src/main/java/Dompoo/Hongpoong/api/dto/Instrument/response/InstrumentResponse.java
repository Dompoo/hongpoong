package Dompoo.Hongpoong.api.dto.Instrument.response;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.enums.Club;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentResponse {
    
    private final Long id;
    private final String type;
    private final String club;
    
    public static InstrumentResponse from(Instrument instrument, Club club) {
        return InstrumentResponse.builder()
                .id(instrument.getId())
                .type(instrument.getType().korName)
                .club(club.korName)
                .build();
    }
}
