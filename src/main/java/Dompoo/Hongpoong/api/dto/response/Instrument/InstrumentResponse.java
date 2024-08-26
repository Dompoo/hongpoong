package Dompoo.Hongpoong.api.dto.response.Instrument;

import Dompoo.Hongpoong.domain.Instrument;
import Dompoo.Hongpoong.domain.enums.Club;
import lombok.Builder;
import lombok.Getter;

@Getter
/*
ResponseBody
{
    "id": 1,
    "product": "장구",
    "club": "산틀",
}
 */
public class InstrumentResponse {
    private Long id;
    private String type;
    private String club;
    
    @Builder
    private InstrumentResponse(Long id, String type, String club) {
        this.id = id;
        this.type = type;
        this.club = club;
    }
    
    public static InstrumentResponse from(Instrument instrument, Club club) {
        return InstrumentResponse.builder()
                .id(instrument.getId())
                .type(instrument.getType())
                .club(club.korName)
                .build();
    }
}
