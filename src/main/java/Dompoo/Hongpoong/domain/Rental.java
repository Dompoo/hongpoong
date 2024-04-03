package Dompoo.Hongpoong.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String fromMember;
    private String toMember;
    private LocalDate date;
    private Integer time;

    @Builder
    public Rental(String product, Integer count, String fromMember, String toMember, LocalDate date, Integer time) {
        this.product = product;
        this.count = count;
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.date = date;
        this.time = time;
    }
}
