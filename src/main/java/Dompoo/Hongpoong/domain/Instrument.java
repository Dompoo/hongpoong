package Dompoo.Hongpoong.domain;

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

    private Instrument.InstrumentType type;

    private boolean available;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public enum InstrumentType {
        KKWANGGWARI("꽹과리"), JANGGU("장구"), BUK("북"), SOGO("소고"), JING("징");

        private final String value;

        InstrumentType(String value) {
            this.value = value;
        }
    }

    @Builder
    public Instrument(InstrumentType type, Member member) {
        this.type = type;
        setMember(member);
        this.available = true;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getInstruments().add(this);
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        reservation.getInstruments().add(this);
    }

    public void returnInstrument() {
        this.reservation = null;
    }
}
