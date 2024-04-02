package Dompoo.Hongpoong.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

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
    private String member;
    private LocalDate date;
    private Integer time;
    private Integer priority;

    @Builder
    public Reservation(String member, LocalDate date, Integer time, Integer priority) {
        this.member = member;
        this.date = date;
        this.time = time;
        this.priority = priority;
    }
}
