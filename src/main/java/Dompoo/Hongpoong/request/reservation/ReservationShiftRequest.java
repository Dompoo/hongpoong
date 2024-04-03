package Dompoo.Hongpoong.request.reservation;

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

    private Integer priority;

    @Builder
    public ReservationShiftRequest(Integer priority) {
        this.priority = priority;
    }
}
