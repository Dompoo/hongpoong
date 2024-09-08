package Dompoo.Hongpoong.api.dto.Instrument.response;

import Dompoo.Hongpoong.domain.entity.Instrument;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentBorrowResponse {
    
    private final Long instrumentId;
    private final LocalDate returnDate;
    private final LocalTime returnTime;
    
    public static InstrumentBorrowResponse from(Instrument instrument) {
        return InstrumentBorrowResponse.builder()
                .instrumentId(instrument.getId())
                .returnDate(instrument.getReservation().getDate())
                .returnTime(instrument.getReservation().getEndTime().localTime)
                .build();
    }
}
