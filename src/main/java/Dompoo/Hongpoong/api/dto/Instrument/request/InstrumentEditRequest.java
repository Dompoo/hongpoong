package Dompoo.Hongpoong.api.dto.Instrument.request;

import Dompoo.Hongpoong.domain.enums.InstrumentType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InstrumentEditRequest {

    private final String type;
    private final Boolean available;
    
    public InstrumentEditDto toDto() {
        return InstrumentEditDto.builder()
                .type(InstrumentType.from(type))
                .available(available)
                .build();
    }
}
