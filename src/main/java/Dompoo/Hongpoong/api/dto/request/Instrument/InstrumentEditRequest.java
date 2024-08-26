package Dompoo.Hongpoong.api.dto.request.Instrument;

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

    private Integer type;
    private Boolean available;

    @Builder
    private InstrumentEditRequest(Integer type, Boolean available) {
        this.type = type;
        this.available = available;
    }
    
    public InstrumentEditDto toDto() {
        return InstrumentEditDto.builder()
                .type(type)
                .available(available)
                .build();
    }
}
