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
    private String product;
    private String club;

    public InstrumentResponse(Instrument instrument) {
        this.id = instrument.getId();
        this.product = instrument.getType().name();
        this.club = instrument.getMember().getClub();
    }
}
