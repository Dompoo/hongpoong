package Dompoo.Hongpoong.api.dto.reservation.response;

import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import Dompoo.Hongpoong.domain.entity.reservation.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationResponse {
    
    @Schema(example = "1")
    private final Long id;
    
    @Schema(example = "이창근")
    private final String username;
    
    @Schema(example = "email@gmail.com")
    private final String email;
    
    @Schema(example = "10")
    private final Integer number;
    
    @Schema(example = "2024-04-17")
    private final LocalDate date;
    
    @Schema(example = "10:00:00")
    private final LocalTime startTime;
    
    @Schema(example = "21:00:00")
    private final LocalTime endTime;
    
    @Schema(example = "산틀 정공 연습")
    private final String message;
    
    @Schema(example = "2024-04-17T20:00:00")
    private final LocalDateTime lastmodified;
    
    public static ReservationResponse from(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .username(reservation.getCreator().getName())
                .email(reservation.getCreator().getEmail())
                .number(reservation.getNumber())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime().localTime)
                .endTime(reservation.getEndTime().localTime)
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