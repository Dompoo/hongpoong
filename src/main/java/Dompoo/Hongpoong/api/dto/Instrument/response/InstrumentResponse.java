package Dompoo.Hongpoong.api.dto.Instrument.response;

import Dompoo.Hongpoong.domain.entity.Instrument;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentResponse {
    
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
    
    public static InstrumentResponse from(Instrument instrument) {
        return InstrumentResponse.builder()
                .instrumentId(instrument.getId())
                .name(instrument.getName())
                .type(instrument.getType().korName)
                .club(instrument.getClub().korName)
                .available(instrument.getAvailable())
                .imageUrl(instrument.getImageUrl())
                .build();
    }
    
    public static List<InstrumentResponse> fromList(List<Instrument> borrowedInstruments) {
        return borrowedInstruments.stream()
                .map(InstrumentResponse::from)
                .toList();
    }
}
