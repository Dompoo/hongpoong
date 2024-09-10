package Dompoo.Hongpoong.api.dto.reservation.request;

import Dompoo.Hongpoong.domain.enums.ReservationTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationEditDto {
    
    private final LocalDate date;
    private final ReservationTime startTime;
    private final ReservationTime endTime;
    private final String message;
    private final List<Long> addedParticipatorIds;
    private final List<Long> removedParticipatorIds;
}
