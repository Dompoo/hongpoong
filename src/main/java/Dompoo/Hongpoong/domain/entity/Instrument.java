package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentEditDto;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.InstrumentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Instrument {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private InstrumentType type;
    
    private Boolean available;
    
    private String imageUrl;
    
    private Club club;
    
    @ManyToOne @JoinColumn(name = "borrower_id")
    private Member borrower;

    @ManyToOne @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    
    public void edit(InstrumentEditDto dto) {
        if (dto.getType() != null) this.type = dto.getType();
        if (dto.getAvailable() != null) this.available = dto.getAvailable();
        if (dto.getImageUrl() != null) this.imageUrl = dto.getImageUrl();
    }
    
    public void borrowInstrument(Member borrower, Reservation reservation) {
        this.borrower = borrower;
        this.reservation = reservation;
        this.available = false;
    }
    
    public void returnInstrument() {
        this.borrower = null;
        this.reservation = null;
        this.available = true;
    }
}
