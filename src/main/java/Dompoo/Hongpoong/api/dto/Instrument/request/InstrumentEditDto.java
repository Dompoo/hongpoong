package Dompoo.Hongpoong.api.dto.Instrument.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentEditDto {

    private final Integer type;
    private final Boolean available;
}
