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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Instrument(String product, Member member) {
        this.product = product;
        setMember(member);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getInstruments().add(this);
    }
}
