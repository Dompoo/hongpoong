package Dompoo.Hongpoong.api.dto.response.Instrument;

import Dompoo.Hongpoong.domain.entity.Instrument;
import lombok.Builder;
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
    
    @Builder
    private InstrumentBorrowResponse(Long instrumentId, LocalDate returnDate, Integer returnTime) {
        this.instrumentId = instrumentId;
        this.returnDate = returnDate;
        this.returnTime = returnTime;
    }
    
    public static InstrumentBorrowResponse from(Instrument instrument) {
        return InstrumentBorrowResponse.builder()
                .instrumentId(instrument.getId())
                .returnDate(instrument.getReservation().getDate())
                .returnTime(instrument.getReservation().getEndTime())
                .build();
    }
}
