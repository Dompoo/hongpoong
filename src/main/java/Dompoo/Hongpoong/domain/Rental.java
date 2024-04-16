package Dompoo.Hongpoong.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/*
{
"대여 풍목": "장구",
"신청자": "산틀",
"대여자": "화랑",
"날짜": "24/04/18",
"시간": 17
}
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String product;
    private Integer count;

    @ManyToOne
    @JoinColumn(name = "from_member")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "to_member")
    private Member toMember;

    private LocalDate date;
    private Integer time;

    @Builder
    public Rental(String product, Integer count, Member fromMember, Member toMember, LocalDate date, Integer time) {
        this.product = product;
        this.count = count;
        setFromMember(fromMember);
        setToMember(toMember);
        this.date = date;
        this.time = time;
    }

    public void setFromMember(Member member) {
        this.fromMember = member;
        member.getGiveRentals().add(this);
    }

    public void setToMember(Member member) {
        this.toMember = member;
        member.getReceiveRentals().add(this);
    }
}
