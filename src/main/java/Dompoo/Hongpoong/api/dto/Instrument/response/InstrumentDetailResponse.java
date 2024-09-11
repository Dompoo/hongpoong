package Dompoo.Hongpoong.api.dto.Instrument.response;

import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
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
    
    @Schema(example = "false")
    private final Boolean available;
    
    @Schema(example = "2024-04-17")
    private final LocalDate returnDate;
    
    @Schema(example = "20:00:00")
    private final LocalTime returnTime;
    
    private final MemberResponse borrower;
    
    public static InstrumentDetailResponse from(Instrument instrument) {
        return InstrumentDetailResponse.builder()
                .instrumentId(instrument.getId())
                .type(instrument.getType().korName)
                .club(instrument.getClub().korName)
                .borrower(instrument.getBorrower() == null ? null : MemberResponse.from(instrument.getBorrower()))
                .available(instrument.isAvailable())
                .returnDate(instrument.getReservation() == null ? null : instrument.getReservation().getDate())
                .returnTime(instrument.getReservation() == null ? null : instrument.getReservation().getEndTime().localTime)
                .build();
    }
}
