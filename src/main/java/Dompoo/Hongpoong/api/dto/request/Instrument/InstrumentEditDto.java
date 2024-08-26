package Dompoo.Hongpoong.api.dto.request.Instrument;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InstrumentEditDto {

    private Integer type;
    private Boolean available;

    @Builder
    private InstrumentEditDto(Integer type, Boolean available) {
        this.type = type;
        this.available = available;
    }
}
