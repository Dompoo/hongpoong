package Dompoo.Hongpoong.domain.entity.reservation;


import Dompoo.Hongpoong.api.dto.request.reservation.ReservationEditDto;
import Dompoo.Hongpoong.domain.entity.Member;
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
    @JoinColumn(name = "creator_id")
    private Member creator;
    private Integer number;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private ReservationTime startTime;
    @Enumerated(EnumType.STRING)
    private ReservationTime endTime;
    private LocalDateTime lastModified;
    @Lob
    private String message;
    
    @Builder
    private Reservation(Member creator, Integer number, LocalDate date, ReservationTime startTime, ReservationTime endTime, LocalDateTime lastModified, String message) {
        this.creator = creator;
        this.number = number;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lastModified = lastModified;
        this.message = message;
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
