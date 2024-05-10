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

    private String product;

    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    @Builder
    public Instrument(String product, Member member) {
        this.product = product;
        setMember(member);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getInstruments().add(this);
    }

    public void setRental(Rental rental) {
        this.rental = rental;
        rental.getInstruments().add(this);
    }
}
