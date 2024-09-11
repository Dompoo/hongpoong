package Dompoo.Hongpoong.api.dto.reservation.response;

import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationEndImage;
import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
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
    
    public static ReservationEndResponse of(Reservation reservation, List<ReservationParticipate> participates, List<ReservationEndImage> reservationEndImages) {
        return ReservationEndResponse.builder()
                .reservationId(reservation.getId())
                .creatorName(reservation.getCreator().getName())
                .email(reservation.getCreator().getEmail())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime().localTime)
                .endTime(reservation.getEndTime().localTime)
                .message(reservation.getMessage())
                .lastmodified(reservation.getLastModified())
                .participators(MemberResponse.fromList(participates.stream().map(ReservationParticipate::getMember).toList()))
                .images(reservationEndImages.stream().map(ReservationEndImage::getImageUrl).toList())
                .build();
    }
}
