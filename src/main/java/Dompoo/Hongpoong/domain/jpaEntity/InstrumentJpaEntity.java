package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.Instrument;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.InstrumentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Enumerated(EnumType.STRING)
    private InstrumentType type;
    
    private Boolean available;
    
    private String imageUrl;
    
    private Club club;
    
    public Instrument toDomain() {
        return Instrument.builder()
                .id(this.id)
                .name(this.name)
                .type(this.type)
                .available(this.available)
                .imageUrl(this.imageUrl)
                .club(this.club)
                .build();
    }
    
    public static InstrumentJpaEntity of(Instrument instrument) {
        return InstrumentJpaEntity.builder()
                .id(instrument.getId())
                .name(instrument.getName())
                .type(instrument.getType())
                .available(instrument.getAvailable())
                .imageUrl(instrument.getImageUrl())
                .club(instrument.getClub())
                .build();
    }
}
