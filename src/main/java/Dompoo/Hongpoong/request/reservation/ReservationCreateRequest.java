package Dompoo.Hongpoong.request.reservation;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
/*
RequestBody
{
    "예약자": "화랑",
    "날짜": "2024-03-17",
    "시간": 18
}
*/
public class ReservationCreateRequest {

    @FutureOrPresent(message = "과거 날짜일 수 없습니다.")
    private LocalDate date;

    @Min(value = 9, message = "9시 이상의 시간이어야 합니다.")
    @Max(value = 22, message = "22시 이하의 시간이어야 합니다.")
    private Integer time;

    @Builder
    public ReservationCreateRequest(LocalDate date, Integer time) {
        this.date = date;
        this.time = time;
    }
}
