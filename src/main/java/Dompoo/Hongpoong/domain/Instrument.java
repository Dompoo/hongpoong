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
    private int count;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Instrument(String product, int count, Member member) {
        this.product = product;
        this.count = count;
        setMember(member);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getInstruments().add(this);
    }
}
