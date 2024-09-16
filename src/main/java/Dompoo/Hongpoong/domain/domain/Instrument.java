package Dompoo.Hongpoong.domain.domain;

import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentEditDto;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.InstrumentType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Instrument {

    private Long id;
    private String name;
    private InstrumentType type;
    private Boolean available;
    private String imageUrl;
    private Club club;
    
    public void edit(InstrumentEditDto dto) {
        if (dto.getName() != null) this.name = dto.getName();
        if (dto.getType() != null) this.type = dto.getType();
        if (dto.getAvailable() != null) this.available = dto.getAvailable();
        if (dto.getImageUrl() != null) this.imageUrl = dto.getImageUrl();
    }
    
    public InstrumentBorrow borrowInstrument(Member borrower, Reservation reservation, LocalDate now) {
        this.available = false;
        
        return InstrumentBorrow.builder()
                .instrument(this)
                .member(borrower)
                .reservation(reservation)
                .borrowDate(now)
                .build();
    }
    
    public void returnInstrument() {
        this.available = true;
    }
}
