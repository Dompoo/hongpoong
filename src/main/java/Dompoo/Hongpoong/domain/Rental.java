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
    @JoinColumn(name = "request_member")
    private Member requestMember;

    @ManyToOne
    @JoinColumn(name = "response_member")
    private Member responseMember;

    private LocalDate date;
    private Integer time;

    @Builder
    public Rental(String product, Integer count, Member responseMember, Member requestMember, LocalDate date, Integer time) {
        this.product = product;
        this.count = count;
        setRequestMember(requestMember);
        setResponseMember(responseMember);
        this.date = date;
        this.time = time;
    }

    public void setRequestMember(Member member) {
        this.requestMember = member;
        member.getReceiveRentals().add(this);
    }

    public void setResponseMember(Member member) {
        this.responseMember = member;
        member.getGiveRentals().add(this);
    }
}
