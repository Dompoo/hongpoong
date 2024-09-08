package Dompoo.Hongpoong.api.dto.response.resevation;

import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import Dompoo.Hongpoong.domain.entity.reservation.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReservationResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final Integer number;
    private final LocalDate date;
    private final String startTime;
    private final String endTime;
    private final String message;
    private final LocalDateTime lastmodified;
    
    @Builder
    private ReservationResponse(Long id, String username, String email, Integer number, LocalDate date, String startTime, String endTime, String message, LocalDateTime lastmodified) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.number = number;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
        this.lastmodified = lastmodified;
    }
    
    public static ReservationResponse from(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .username(reservation.getCreator().getName())
                .email(reservation.getCreator().getEmail())
                .number(reservation.getNumber())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime().strValue)
                .endTime(reservation.getEndTime().strValue)
                .message(reservation.getMessage())
                .lastmodified(reservation.getLastModified())
                .build();
    }
    
    public static List<ReservationResponse> fromList(List<Reservation> reservations) {
        return reservations.stream().map(ReservationResponse::from).toList();
    }
    
    public static List<ReservationResponse> fromParticipateList(List<ReservationParticipate> reservations) {
        return reservations.stream().map(rp -> ReservationResponse.from(rp.getReservation())).toList();
    }
}
