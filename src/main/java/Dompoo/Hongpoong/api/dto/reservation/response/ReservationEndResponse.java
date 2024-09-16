package Dompoo.Hongpoong.api.dto.reservation.response;

import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationEndImageJpaEntity;
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
public class ReservationEndResponse {
    
    @Schema(example = "1")
    private final Long reservationId;
    
    @Schema(example = "이창근")
    private final String creatorName;
    
    @Schema(example = "email@gmail.com")
    private final String email;
    
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
    
    private final List<MemberResponse> participators;
    
    @Schema(example = "[image.com/1, image.com/2]")
    private final List<String> images;
    
    public static ReservationEndResponse of(ReservationJpaEntity reservationJpaEntity, List<AttendanceJpaEntity> participates, List<ReservationEndImageJpaEntity> reservationEndImageJpaEntities) {
        return ReservationEndResponse.builder()
                .reservationId(reservationJpaEntity.getId())
                .creatorName(reservationJpaEntity.getCreator().getName())
                .email(reservationJpaEntity.getCreator().getEmail())
                .date(reservationJpaEntity.getDate())
                .startTime(reservationJpaEntity.getStartTime().localTime)
                .endTime(reservationJpaEntity.getEndTime().localTime)
                .message(reservationJpaEntity.getMessage())
                .lastmodified(reservationJpaEntity.getLastModified())
                .participators(MemberResponse.fromList(participates.stream().map(AttendanceJpaEntity::getMemberJpaEntity).toList()))
                .images(reservationEndImageJpaEntities.stream().map(ReservationEndImageJpaEntity::getImageUrl).toList())
                .build();
    }
}
