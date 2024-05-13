package Dompoo.Hongpoong.request.Instrument;

import Dompoo.Hongpoong.domain.Instrument;
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
    "product": "장구"
}
 */
public class InstrumentCreateRequest {

    private Instrument.InstrumentType type;

    @Builder
    public InstrumentCreateRequest(Instrument.InstrumentType type) {
        this.type = type;
    }
}
