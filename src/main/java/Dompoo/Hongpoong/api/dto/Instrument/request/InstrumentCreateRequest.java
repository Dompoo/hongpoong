package Dompoo.Hongpoong.api.dto.Instrument.request;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.enums.InstrumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InstrumentCreateRequest {

    @NotNull(message = "악기는 비어있을 수 없습니다.")
    @Schema(enumAsRef = true)
    private final InstrumentType type;
    
    public Instrument toInstrument(Member member) {
        return Instrument.builder()
                .type(type)
                .member(member)
                .build();
    }
}
