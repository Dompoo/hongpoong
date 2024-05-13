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
    "type": "1",
    "available": false
}
 */
public class InstrumentEditRequest {

    private Instrument.InstrumentType type;
    private Boolean available;

    @Builder
    public InstrumentEditRequest(Instrument.InstrumentType type, boolean available) {
        this.type = type;
        this.available = available;
    }
}
