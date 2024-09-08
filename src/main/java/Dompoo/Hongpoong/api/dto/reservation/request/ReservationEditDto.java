package Dompoo.Hongpoong.api.dto.reservation.request;

import Dompoo.Hongpoong.domain.entity.reservation.ReservationTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationEditDto {
    
    private final Integer number;
    private final LocalDate date;
    private final ReservationTime startTime;
    private final ReservationTime endTime;
    private final String message;
}