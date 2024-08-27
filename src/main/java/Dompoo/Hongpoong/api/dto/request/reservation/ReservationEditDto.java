package Dompoo.Hongpoong.api.dto.request.reservation;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReservationEditDto {
    
    private Integer number;
    private LocalDate date;
    private Integer startTime;
    private Integer endTime;
    private String message;

    @Builder
    private ReservationEditDto(Integer number, LocalDate date, Integer startTime, Integer endTime, String message) {
        this.number = number;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
    }
}
