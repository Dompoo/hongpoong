package Dompoo.Hongpoong.api.dto.reservation.response;

import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
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
    private final Long reservationId;
    
    @Schema(example = "이창근")
    private final String creatorName;
    
    @Schema(example = "2024-04-17")
    private final LocalDate date;
    
    @Schema(example = "정기연습")
    private final String type;
    
    @Schema(example = "10:00:00")
    private final LocalTime startTime;
    
    @Schema(example = "21:00:00")
    private final LocalTime endTime;
    
    @Schema(example = "산틀 정공 연습")
    private final String message;
    
    @Schema(example = "true")
    private final Boolean participationAvailable;
    
    @Schema(example = "2024-04-17T20:00:00")
    private final LocalDateTime lastmodified;
    
    public static ReservationResponse from(ReservationJpaEntity reservationJpaEntity) {
        return ReservationResponse.builder()
                .reservationId(reservationJpaEntity.getId())
                .creatorName(reservationJpaEntity.getCreator().getName())
                .date(reservationJpaEntity.getDate())
                .type(reservationJpaEntity.getType().korName)
                .startTime(reservationJpaEntity.getStartTime().localTime)
                .endTime(reservationJpaEntity.getEndTime().localTime)
                .message(reservationJpaEntity.getMessage())
                .participationAvailable(reservationJpaEntity.getParticipationAvailable())
                .lastmodified(reservationJpaEntity.getLastModified())
                .build();
    }
    
    public static List<ReservationResponse> fromList(List<ReservationJpaEntity> reservationJpaEntities) {
        return reservationJpaEntities.stream().map(ReservationResponse::from).toList();
    }
    
    public static List<ReservationResponse> fromParticipateList(List<AttendanceJpaEntity> reservations) {
        return reservations.stream().map(rp -> ReservationResponse.from(rp.getReservation())).toList();
    }
}
