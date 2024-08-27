package Dompoo.Hongpoong.domain;


import Dompoo.Hongpoong.api.dto.request.reservation.ReservationEditDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

//    @OneToMany(mappedBy = "reservation")
//    private List<Instrument> instruments = new ArrayList<>();

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
    
    public void edit(ReservationEditDto dto, LocalDateTime now) {
        if (dto.getNumber() != null) this.number = dto.getNumber();
        if (dto.getDate() != null) this.date = dto.getDate();
        if (dto.getStartTime() != null) this.startTime = dto.getStartTime();
        if (dto.getEndTime() != null) this.endTime = dto.getEndTime();
        if (dto.getMessage() != null) this.message = dto.getMessage();
        this.lastModified = now;
    }
}
