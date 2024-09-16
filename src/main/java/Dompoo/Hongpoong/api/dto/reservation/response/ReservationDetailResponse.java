package Dompoo.Hongpoong.api.dto.reservation.response;

import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
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
public class ReservationDetailResponse {
    
    @Schema(example = "1")
    private final Long reservationId;
    
    @Schema(example = "이창근")
    private final String creatorName;
    
    @Schema(example = "email@gmail.com")
    private final String email;
    
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
    
    private final List<MemberResponse> participators;
    
    public static ReservationDetailResponse of(ReservationJpaEntity reservationJpaEntity, List<MemberJpaEntity> participators) {
        return ReservationDetailResponse.builder()
                .reservationId(reservationJpaEntity.getId())
                .creatorName(reservationJpaEntity.getCreator().getName())
                .email(reservationJpaEntity.getCreator().getEmail())
                .date(reservationJpaEntity.getDate())
                .type(reservationJpaEntity.getType().korName)
                .startTime(reservationJpaEntity.getStartTime().localTime)
                .endTime(reservationJpaEntity.getEndTime().localTime)
                .message(reservationJpaEntity.getMessage())
                .lastmodified(reservationJpaEntity.getLastModified())
                .participationAvailable(reservationJpaEntity.getParticipationAvailable())
                .participators(MemberResponse.fromList(participators))
                .build();
    }
}
