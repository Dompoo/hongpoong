package Dompoo.Hongpoong.api.dto.Instrument.request;

import Dompoo.Hongpoong.domain.enums.InstrumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InstrumentEditRequest {
    
    @Schema(example = "검정 장구 8.5치")
    private final String name;
    
    @Schema(enumAsRef = true)
    private final InstrumentType type;
    
    @Schema(example = "true")
    private final Boolean available;
    
    @Schema(example = "image.com/1")
    private final String imageUrl;
    
    public InstrumentEditDto toDto() {
        return InstrumentEditDto.builder()
                .name(name)
                .type(type)
                .available(available)
                .imageUrl(imageUrl)
                .build();
    }
}
