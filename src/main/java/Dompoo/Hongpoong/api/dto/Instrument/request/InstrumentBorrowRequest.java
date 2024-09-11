package Dompoo.Hongpoong.api.dto.Instrument.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InstrumentBorrowRequest {

    @NotNull(message = "악기 대여할 예약은 비어있을 수 없습니다.")
    @Schema(example = "1")
    private final Long reservationId;
}
