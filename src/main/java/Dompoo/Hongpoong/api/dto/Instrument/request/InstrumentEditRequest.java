package Dompoo.Hongpoong.api.dto.Instrument.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InstrumentEditRequest {

    private final Integer type;
    private final Boolean available;
    
    public InstrumentEditDto toDto() {
        return InstrumentEditDto.builder()
                .type(type)
                .available(available)
                .build();
    }
}
