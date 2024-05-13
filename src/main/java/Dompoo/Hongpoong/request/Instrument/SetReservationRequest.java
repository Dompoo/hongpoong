package Dompoo.Hongpoong.request.Instrument;

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

    private Long reservationId;

    @Builder
    public SetReservationRequest(Long reservationId) {
        this.reservationId = reservationId;
    }
}
