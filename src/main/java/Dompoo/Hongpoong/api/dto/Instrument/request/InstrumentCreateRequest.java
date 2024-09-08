package Dompoo.Hongpoong.api.dto.Instrument.request;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.enums.InstrumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InstrumentCreateRequest {

    @NotBlank(message = "악기는 비어있을 수 없습니다.")
    @Schema(example = "장구")
    private final String type;
    
    public Instrument toInstrument(Member member) {
        return Instrument.builder()
                .type(InstrumentType.from(type))
                .member(member)
                .build();
    }
}
