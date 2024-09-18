package Dompoo.Hongpoong.domain.domain;

import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.enums.ReservationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reservation {

    private final Long id;
    private final LocalDate date;
    private final ReservationType type;
    private final ReservationTime startTime;
    private final ReservationTime endTime;
    private final LocalDateTime lastModified;
    private final String message;
    private final Boolean participationAvailable;
    private final Member creator;
    
    public Reservation withEdited(LocalDate date, ReservationTime startTime, ReservationTime endTime, String message, LocalDateTime now) {
        return Reservation.builder()
                .id(this.id)
                .date(date == null ? this.date : date)
                .type(this.type)
                .startTime(startTime == null ? this.startTime : startTime)
                .endTime(endTime == null ? this.endTime : endTime)
                .lastModified(now)
                .message(message == null ? this.message : message)
                .participationAvailable(this.participationAvailable)
                .creator(this.creator)
                .build();
    }
    
    public Reservation withExtendEndTime() {
        return Reservation.builder()
                .id(this.id)
                .date(this.date)
                .type(this.type)
                .startTime(this.startTime)
                .endTime(this.endTime.nextReservationTime())
                .lastModified(this.lastModified)
                .message(this.message)
                .participationAvailable(this.participationAvailable)
                .creator(this.creator)
                .build();
    }
}
