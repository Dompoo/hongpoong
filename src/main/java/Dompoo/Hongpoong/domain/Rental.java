package Dompoo.Hongpoong.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "rental")
    private List<Instrument> instruments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "request_member")
    private Member requestMember;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Builder
    public Rental(Member requestMember, Reservation reservation) {
        setRequestMember(requestMember);
        setReservation(reservation);
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        reservation.setRental(this);
    }
}
