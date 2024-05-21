package Dompoo.Hongpoong.request.Instrument;

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
    public InstrumentCreateRequest(Integer type) {
        this.type = type;
    }
}
