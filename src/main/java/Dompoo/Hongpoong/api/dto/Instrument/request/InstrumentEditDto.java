package Dompoo.Hongpoong.api.dto.Instrument.request;

import Dompoo.Hongpoong.domain.enums.InstrumentType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentEditDto {

    private final InstrumentType type;
    private final Boolean available;
}
