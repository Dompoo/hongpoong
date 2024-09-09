package Dompoo.Hongpoong.api.dto.Instrument.request;

import Dompoo.Hongpoong.domain.enums.InstrumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InstrumentEditRequest {
    
    @Schema(example = "장구")
    private final String type;
    
    @Schema(example = "true")
    private final Boolean available;
    
    public InstrumentEditDto toDto() {
        return InstrumentEditDto.builder()
                .type(InstrumentType.from(type))
                .available(available)
                .build();
    }
}
