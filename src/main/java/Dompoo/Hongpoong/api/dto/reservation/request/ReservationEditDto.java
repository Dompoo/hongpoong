package Dompoo.Hongpoong.api.dto.reservation.request;

import Dompoo.Hongpoong.domain.enums.ReservationTime;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    private final List<Long> addedBorrowInstrumentIds;
    private final List<Long> removedBorrowInstrumentIds;
}
