package Dompoo.Hongpoong.api.dto.reservation.request;

import Dompoo.Hongpoong.domain.enums.ReservationTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class ReservationEditRequest {

    @FutureOrPresent(message = "과거 날짜일 수 없습니다.")
    @Schema(example = "2024-04-17")
    private final LocalDate date;
    
    @Schema(example = "10:00:00")
    private final LocalTime startTime;
    
    @Schema(example = "21:00:00")
    private final LocalTime endTime;
    
    @Schema(example = "산틀 정공 연습")
    private final String message;
    
    @Schema(example = "{1, 2, 3}")
    private final List<Long> addedParticipatorIds;
    
    @Schema(example = "{4, 5, 6}")
    private final List<Long> removedParticipatorIds;
    
    public ReservationEditDto toDto() {
        return ReservationEditDto.builder()
                .date(date)
                .startTime(ReservationTime.from(startTime))
                .endTime(ReservationTime.from(endTime))
                .message(message)
                .addedParticipatorIds(addedParticipatorIds)
                .removedParticipatorIds(removedParticipatorIds)
                .build();
    }
}
