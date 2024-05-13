package Dompoo.Hongpoong.request.Instrument;

import Dompoo.Hongpoong.domain.Instrument;
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
    "type": "1"
}
 */
public class InstrumentCreateRequest {

    @NotNull(message = "악기는 비어있을 수 없습니다.")
    private Instrument.InstrumentType type;

    @Builder
    public InstrumentCreateRequest(Instrument.InstrumentType type) {
        this.type = type;
    }
}
