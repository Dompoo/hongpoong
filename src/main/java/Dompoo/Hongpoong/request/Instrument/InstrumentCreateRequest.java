package Dompoo.Hongpoong.request.Instrument;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstrumentCreateRequest {

    private String product;

    @Builder
    public InstrumentCreateRequest(String product) {
        this.product = product;
    }
}
