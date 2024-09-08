package Dompoo.Hongpoong.api.dto.Instrument.response;

import Dompoo.Hongpoong.domain.entity.Instrument;
import io.swagger.v3.oas.annotations.media.Schema;
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
    
    @Schema(example = "1")
    private final Long instrumentId;
    
    @Schema(example = "2024-04-17")
    private final LocalDate returnDate;
    
    @Schema(example = "20:00:00")
    private final LocalTime returnTime;
    
    public static InstrumentBorrowResponse from(Instrument instrument) {
        return InstrumentBorrowResponse.builder()
                .instrumentId(instrument.getId())
                .returnDate(instrument.getReservation().getDate())
                .returnTime(instrument.getReservation().getEndTime().localTime)
                .build();
    }
}
