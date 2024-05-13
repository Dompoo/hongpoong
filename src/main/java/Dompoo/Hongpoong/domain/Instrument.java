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

    private String name;

    private boolean available;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Builder
    public Instrument(String name, Member member) {
        this.name = name;
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
