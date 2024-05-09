package Dompoo.Hongpoong.response.Instrument;

import Dompoo.Hongpoong.domain.Instrument;
import Dompoo.Hongpoong.domain.Member;
import lombok.Getter;

@Getter
public class InstrumentResponse {
    private Long id;
    private String product;
    private Member.Club club;

    public InstrumentResponse(Instrument instrument) {
        this.id = instrument.getId();
        this.product = instrument.getProduct();
        this.club = Member.Club.valueOf(instrument.getMember().getClub());
    }
}
