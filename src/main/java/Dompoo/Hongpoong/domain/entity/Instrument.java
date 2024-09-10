package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentEditDto;
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
    
    private boolean available;
    
    private String imageUrl;

    @ManyToOne @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    
    public void edit(InstrumentEditDto dto) {
        if (dto.getType() != null) this.type = dto.getType();
        if (dto.getAvailable() != null) this.available = dto.getAvailable();
        if (dto.getImageUrl() != null) this.imageUrl = dto.getImageUrl();
    }
    
    public void borrowInstrument(Reservation reservation) {
        this.reservation = reservation;
        this.available = false;
    }
    
    public void returnInstrument() {
        this.reservation = null;
        this.available = true;
    }
}
