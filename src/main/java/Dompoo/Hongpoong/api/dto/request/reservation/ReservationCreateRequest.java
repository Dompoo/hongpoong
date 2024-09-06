package Dompoo.Hongpoong.api.dto.request.reservation;

import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.reservation.Reservation;
import Dompoo.Hongpoong.domain.entity.reservation.ReservationTime;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReservationCreateRequest {

    @NotNull(message = "인원수를 입력하세요.")
    private Integer number;

    @FutureOrPresent(message = "과거 날짜일 수 없습니다.")
    private LocalDate date;

    @NotBlank(message = "시작 시간을 입력하세요.")
    private String startTime;

    @NotBlank(message = "종료 시간을 입력하세요.")
    private String endTime;
    
    @NotNull(message = "참가자를 입력하세요.")
    private List<Long> participaterIds;

    private String message = "";
    
    @Builder
    private ReservationCreateRequest(Integer number, LocalDate date, String startTime, String endTime, List<Long> participaterIds, String message) {
        this.number = number;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participaterIds = participaterIds;
        this.message = message;
    }
    
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
