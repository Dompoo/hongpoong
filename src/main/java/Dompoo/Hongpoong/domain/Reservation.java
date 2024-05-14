package Dompoo.Hongpoong.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
{
"예약 ID": 1
"날짜": "2024-03-17",
"예약자": "화랑",
"시간": 18,
"예약순서" : 1
}
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer number;

    @OneToMany(mappedBy = "reservation")
    private List<Instrument> instruments = new ArrayList<>();

    private LocalDate date;
    private Integer time;
    private Integer priority;

    @Lob
    private String message;

    @Builder
    public Reservation(Member member, Integer number, LocalDate date, Integer time, Integer priority, String message) {
        setMember(member);
        this.number = number;
        this.date = date;
        this.time = time;
        this.priority = priority;
        this.message = message;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getReservations().add(this);
    }
}
