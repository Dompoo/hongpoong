package Dompoo.Hongpoong.api.dto.Instrument.response;

import Dompoo.Hongpoong.domain.jpaEntity.InstrumentBorrowJpaEntity;
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
    
    public static InstrumentBorrowResponse from(InstrumentBorrowJpaEntity instrumentBorrowJpaEntity) {
        return InstrumentBorrowResponse.builder()
                .borrowerName(instrumentBorrowJpaEntity.getMemberJpaEntity().getName())
                .borrowerNickname(instrumentBorrowJpaEntity.getMemberJpaEntity().getNickname())
                .borrowDate(instrumentBorrowJpaEntity.getBorrowDate())
                .build();
    }
    
    public static List<InstrumentBorrowResponse> fromList(List<InstrumentBorrowJpaEntity> instrumentBorrowJpaEntities) {
        return instrumentBorrowJpaEntities.stream().map(InstrumentBorrowResponse::from).toList();
    }
}
