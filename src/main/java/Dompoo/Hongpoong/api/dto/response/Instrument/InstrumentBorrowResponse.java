package Dompoo.Hongpoong.api.dto.response.Instrument;

import Dompoo.Hongpoong.domain.entity.Instrument;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class InstrumentBorrowResponse {
    private final Long instrumentId;
    private final LocalDate returnDate;
    private final LocalTime returnTime;
    
    @Builder
    private InstrumentBorrowResponse(Long instrumentId, LocalDate returnDate, LocalTime returnTime) {
        this.instrumentId = instrumentId;
        this.returnDate = returnDate;
        this.returnTime = returnTime;
    }
    
    public static InstrumentBorrowResponse from(Instrument instrument) {
        return InstrumentBorrowResponse.builder()
                .instrumentId(instrument.getId())
                .returnDate(instrument.getReservation().getDate())
                .returnTime(instrument.getReservation().getEndTime().localTime)
                .build();
    }
}
