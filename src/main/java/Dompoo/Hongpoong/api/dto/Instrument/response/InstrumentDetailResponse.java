package Dompoo.Hongpoong.api.dto.Instrument.response;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.InstrumentBorrow;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentDetailResponse {
    
    @Schema(example = "1")
    private final Long instrumentId;
    
    @Schema(example = "검정 장구 8.5치")
    private final String name;
    
    @Schema(example = "장구")
    private final String type;
    
    @Schema(example = "산틀")
    private final String club;
    
    @Schema(example = "false")
    private final Boolean available;
    
    @Schema(example = "image.com/1")
    private final String imageUrl;
    
    private final List<InstrumentBorrowResponse> borrowHistory;
    
    public static InstrumentDetailResponse from(Instrument instrument, List<InstrumentBorrow> instrumentBorrows) {
        return InstrumentDetailResponse.builder()
                .instrumentId(instrument.getId())
                .name(instrument.getName())
                .type(instrument.getType().korName)
                .club(instrument.getClub().korName)
                .available(instrument.getAvailable())
                .imageUrl(instrument.getImageUrl())
                .borrowHistory(InstrumentBorrowResponse.fromList(instrumentBorrows))
                .build();
    }
}
