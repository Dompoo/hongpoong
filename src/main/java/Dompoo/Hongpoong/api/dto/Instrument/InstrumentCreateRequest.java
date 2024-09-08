package Dompoo.Hongpoong.api.dto.Instrument;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.enums.InstrumentType;
import jakarta.validation.constraints.NotNull;
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
    "type": 1
}
 */
public class InstrumentCreateRequest {

    @NotNull(message = "악기는 비어있을 수 없습니다.")
    private Integer type;

    @Builder
    private InstrumentCreateRequest(Integer type) {
        this.type = type;
    }
    
    public Instrument toInstrument(Member member) {
        return Instrument.builder()
                .type(InstrumentType.fromInt(type))
                .member(member)
                .build();
    }
}
