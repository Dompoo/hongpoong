package Dompoo.Hongpoong.api.dto.Instrument.request;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.InstrumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InstrumentCreateRequest {
    
    @NotBlank(message = "악기 이름은 비어있을 수 없습니다.")
    @Schema(example = "검정 장구 8.5치")
    private final String name;

    @NotNull(message = "악기 종류는 비어있을 수 없습니다.")
    @Schema(enumAsRef = true)
    private final InstrumentType type;
    
    @Schema(example = "image.com/1")
    private final String imageUrl;
    
    public Instrument toInstrument(Club club) {
        return Instrument.builder()
                .name(name)
                .type(type)
                .club(club)
                .available(true)
                .imageUrl(imageUrl)
                .build();
    }
}
