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
    "date": "2024-12-18",
    "startTime": 18
    "endTime": 22
}
 */
public class ReservationCreateRequest {

    @FutureOrPresent(message = "과거 날짜일 수 없습니다.")
    private LocalDate date;

    @Min(value = 9, message = "9시 이상의 시간이어야 합니다.")
    @Max(value = 22, message = "22시 이하의 시간이어야 합니다.")
    private Integer startTime;

    @Min(value = 9, message = "9시 이상의 시간이어야 합니다.")
    @Max(value = 22, message = "22시 이하의 시간이어야 합니다.")
    private Integer endTime;

    @Builder
    public ReservationCreateRequest(LocalDate date, Integer startTime, Integer endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
