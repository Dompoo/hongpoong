package Dompoo.Hongpoong.api.dto.reservation.response;

import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import Dompoo.Hongpoong.domain.entity.reservation.Reservation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
