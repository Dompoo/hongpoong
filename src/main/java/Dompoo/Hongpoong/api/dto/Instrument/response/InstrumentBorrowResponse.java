package Dompoo.Hongpoong.api.dto.Instrument.response;

import Dompoo.Hongpoong.domain.entity.InstrumentBorrow;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentBorrowResponse {
    
    @Schema(example = "이창근")
    private final String borrowerName;
    
    @Schema(example = "불꽃남자")
    private final String borrowerNickname;
    
    @Schema(example = "2024-05-17")
    private final LocalDate borrowDate;
    
    public static InstrumentBorrowResponse from(InstrumentBorrow instrumentBorrow) {
        return InstrumentBorrowResponse.builder()
                .borrowerName(instrumentBorrow.getMember().getName())
                .borrowerNickname(instrumentBorrow.getMember().getNickname())
                .borrowDate(instrumentBorrow.getBorrowDate())
                .build();
    }
    
    public static List<InstrumentBorrowResponse> fromList(List<InstrumentBorrow> instrumentBorrows) {
        return instrumentBorrows.stream().map(InstrumentBorrowResponse::from).toList();
    }
}
