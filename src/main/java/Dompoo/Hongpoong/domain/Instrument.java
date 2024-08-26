package Dompoo.Hongpoong.domain;

import Dompoo.Hongpoong.domain.enums.InstrumentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private InstrumentType type;

    private boolean available;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Builder
    public Instrument(InstrumentType type, Member member) {
        setMember(member);
        this.type = type;
        this.available = true;
    }

    public String getType() {
        return type.name();
    }
}
