package Dompoo.Hongpoong.api.dto.response.Instrument;

import Dompoo.Hongpoong.domain.Instrument;
import lombok.Getter;

import java.time.LocalDate;

@Getter
/*
ResponseBody
{
    "instrumentId": 1,
    "returnDate": "24/04/18",
    "returnTime": 14",
}
 */
public class InstrumentBorrowResponse {
    private Long instrumentId;
    private LocalDate returnDate;
    private Integer returnTime;

    public InstrumentBorrowResponse(Instrument instrument) {
        this.instrumentId = instrument.getId();
        this.returnDate = instrument.getReservation().getDate();
        this.returnTime = instrument.getReservation().getEndTime();
    }
}
