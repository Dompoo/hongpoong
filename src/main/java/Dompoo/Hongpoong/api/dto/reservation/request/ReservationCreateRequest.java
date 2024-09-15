package Dompoo.Hongpoong.api.dto.reservation.request;

import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.enums.ReservationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class ReservationCreateRequest {

    @NotNull(message = "예약 날짜는 비어있을 수 없습니다.")
    @FutureOrPresent(message = "과거 날짜일 수 없습니다.")
    @Schema(example = "2024-04-17")
    private final LocalDate date;
    
    @NotNull(message = "예약 종류는 비어있을 수 없습니다.")
    @Schema(enumAsRef = true)
    private final ReservationType type;

    @NotNull(message = "시작 시간은 비어있을 수 없습니다.")
    @Schema(enumAsRef = true)
    private final ReservationTime startTime;

    @NotNull(message = "종료 시간은 비어있을 수 없습니다.")
    @Schema(enumAsRef = true)
    private final ReservationTime endTime;
    
    @NotNull(message = "참가자는 비어있을 수 없습니다.")
    @Schema(example = "[1, 2, 3]")
    private final List<Long> participaterIds;
    
    @Schema(example = "산틀 정공 연습")
    private final String message;
    
    public Reservation toReservation(Member creator) {
        return Reservation.builder()
                .creator(creator)
                .date(date)
                .type(type)
                .startTime(startTime)
                .endTime(endTime)
                .message(message)
                .build();
    }
}
