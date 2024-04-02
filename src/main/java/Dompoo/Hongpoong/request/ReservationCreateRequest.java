package Dompoo.Hongpoong.request;

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

    private String member;
    private LocalDate date;
    private Integer time;

    @Builder
    public ReservationCreateRequest(String member, LocalDate date, Integer time) {
        this.member = member;
        this.date = date;
        this.time = time;
    }
}
