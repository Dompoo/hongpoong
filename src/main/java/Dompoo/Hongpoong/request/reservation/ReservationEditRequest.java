package Dompoo.Hongpoong.request.reservation;

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
    "날짜": 240317,
    "시간": 2030
}
*/
public class ReservationEditRequest {

    private LocalDate date;
    private Integer time;

    @Builder
    public ReservationEditRequest(LocalDate date, Integer time) {
        this.date = date;
        this.time = time;
    }
}
