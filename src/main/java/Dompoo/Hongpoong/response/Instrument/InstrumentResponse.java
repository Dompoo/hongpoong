package Dompoo.Hongpoong.response.Instrument;

import Dompoo.Hongpoong.domain.Instrument;
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

    public InstrumentResponse(Instrument instrument) {
        this.id = instrument.getId();
        this.type = instrument.getType();
        this.club = instrument.getMember().getClub();
    }
}
