package Dompoo.Hongpoong.api.dto.Instrument.request;

import Dompoo.Hongpoong.domain.enums.InstrumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InstrumentEditRequest {
    
    @NotBlank(message = "악기는 비어있을 수 없습니다.")
    @Schema(example = "장구")
    private final String type;
    
    @NotNull(message = "사용가능 여부는 비어있을 수 없습니다.")
    @Schema(example = "true")
    private final Boolean available;
    
    public InstrumentEditDto toDto() {
        return InstrumentEditDto.builder()
                .type(InstrumentType.from(type))
                .available(available)
                .build();
    }
}
