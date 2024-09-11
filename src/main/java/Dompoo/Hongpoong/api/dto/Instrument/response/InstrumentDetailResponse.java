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
public class InstrumentDetailResponse {
    
    @Schema(example = "1")
    private final Long instrumentId;
    
    @Schema(example = "장구")
    private final String type;
    
    @Schema(example = "산틀")
    private final String club;
    
    @Schema(example = "1")
    private final Long borrowerId;
    
    @Schema(example = "false")
    private final Boolean available;
    
    @Schema(example = "2024-04-17")
    private final LocalDate returnDate;
    
    @Schema(example = "20:00:00")
    private final LocalTime returnTime;
    
    public static InstrumentDetailResponse from(Instrument instrument) {
        return InstrumentDetailResponse.builder()
                .instrumentId(instrument.getId())
                .type(instrument.getType().korName)
                .club(instrument.getClub().korName)
                .borrowerId(instrument.getBorrower().getId())
                .available(instrument.isAvailable())
                .returnDate(instrument.getReservation().getDate())
                .returnTime(instrument.getReservation().getEndTime().localTime)
                .build();
    }
}
