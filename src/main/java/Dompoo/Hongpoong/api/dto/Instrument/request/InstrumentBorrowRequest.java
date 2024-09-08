package Dompoo.Hongpoong.api.dto.Instrument.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InstrumentBorrowRequest {

    @NotNull(message = "악기 대여할 예약은 비어있을 수 없습니다.")
    private final Long reservationId;

    @NotNull(message = "악기는 비어있을 수 없습니다.")
    private final Long instrumentId;
}
