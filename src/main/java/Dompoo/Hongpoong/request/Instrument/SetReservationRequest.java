package Dompoo.Hongpoong.request.Instrument;

import jakarta.validation.constraints.NotNull;
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
    "reservationId": 1,
}
 */
public class SetReservationRequest {

    @NotNull(message = "악기 대여할 예약은 비어있을 수 없습니다.")
    private Long reservationId;

    @Builder
    public SetReservationRequest(Long reservationId) {
        this.reservationId = reservationId;
    }
}
