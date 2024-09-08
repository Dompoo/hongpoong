package Dompoo.Hongpoong.api.dto.reservation.request;

import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.reservation.Reservation;
import Dompoo.Hongpoong.domain.entity.reservation.ReservationTime;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class ReservationCreateRequest {

    @NotNull(message = "인원수를 입력하세요.")
    private final Integer number;

    @FutureOrPresent(message = "과거 날짜일 수 없습니다.")
    private final LocalDate date;

    @NotBlank(message = "시작 시간을 입력하세요.")
    private final String startTime;

    @NotBlank(message = "종료 시간을 입력하세요.")
    private final String endTime;
    
    @NotNull(message = "참가자를 입력하세요.")
    private final List<Long> participaterIds;

    private final String message = "";
    
    public Reservation toReservation(Member creator) {
        return Reservation.builder()
                .creator(creator)
                .number(number)
                .date(date)
                .startTime(ReservationTime.from(startTime))
                .endTime(ReservationTime.from(endTime))
                .message(message)
                .build();
    }
}
