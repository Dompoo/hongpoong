package Dompoo.Hongpoong.request.Instrument;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
/*
RequestBody
{
    "product": "장구",
    "available": false
}
 */
public class InstrumentEditRequest {

    private String product;
    private Boolean available;

    public InstrumentEditRequest(String product, boolean available) {
        this.product = product;
        this.available = available;
    }
}
