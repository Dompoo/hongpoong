package Dompoo.Hongpoong.api.dto.reservation;

import Dompoo.Hongpoong.domain.entity.reservation.ReservationTime;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReservationEditDto {
    
    private Integer number;
    private LocalDate date;
    private ReservationTime startTime;
    private ReservationTime endTime;
    private String message;
    
    @Builder
    private ReservationEditDto(Integer number, LocalDate date, ReservationTime startTime, ReservationTime endTime, String message) {
        this.number = number;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
    }
}
