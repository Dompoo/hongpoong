package Dompoo.Hongpoong.response;

import Dompoo.Hongpoong.domain.Reservation;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MenuResponse {

    private final Long id;
    private final String username;
    private final LocalDate date;
    private final Integer time;
    private final Integer priority;

    public MenuResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.username = reservation.getMember().getUsername();
        this.date = reservation.getDate();
        this.time = reservation.getTime();
        this.priority = reservation.getPriority();
    }
}
