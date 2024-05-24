package Dompoo.Hongpoong.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private Integer startTime;
    private Integer endTime;
    private LocalDateTime lastModified;

    @Lob
    private String message;

    @Builder
    public Reservation(Member member, Integer number, LocalDate date, Integer startTime, Integer endTime, String message) {
        setMember(member);
        this.number = number;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
        lastModified = LocalDateTime.now();
    }

    public void setMember(Member member) {
        this.member = member;
        member.getReservations().add(this);
    }
}
