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
    "number": 3,
    "date": "2024-12-18",
    "time": 18
    "message": "산틀 정기공연 연습"
}
 */
public class ReservationEditRequest {

    private Integer number;

    @FutureOrPresent(message = "과거 날짜일 수 없습니다.")
    private LocalDate date;

    @Min(value = 9, message = "9시 이상의 시간이어야 합니다.")
    @Max(value = 22, message = "22시 이하의 시간이어야 합니다.")
    private Integer time;

    private String message;

    @Builder
    public ReservationEditRequest(Integer number, LocalDate date, Integer time, String message) {
        this.number = number;
        this.date = date;
        this.time = time;
        this.message = message;
    }
}
