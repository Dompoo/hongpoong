package Dompoo.Hongpoong.request.reservation;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
/*
RequestBody
{
    "예약순서": 3
}
*/
public class ReservationShiftRequest {

    @Min(value = 1, message = "우선순위는 최대 1입니다.")
    private Integer priority;

    @Builder
    public ReservationShiftRequest(Integer priority) {
        this.priority = priority;
    }
}
