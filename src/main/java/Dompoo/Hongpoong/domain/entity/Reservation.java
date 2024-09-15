package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditDto;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.enums.ReservationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Supplier;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    private ReservationType type;
    
    @Enumerated(EnumType.STRING)
    private ReservationTime startTime;
    
    @Enumerated(EnumType.STRING)
    private ReservationTime endTime;
    
    private LocalDateTime lastModified;
    
    private String message;
    
    private Boolean participationAvailable;
    
    @ManyToOne @JoinColumn(name = "creator_id")
    private Member creator;
    
    public void edit(ReservationEditDto dto, LocalDateTime now) {
        if (dto.getDate() != null) this.date = dto.getDate();
        if (dto.getStartTime() != null) this.startTime = dto.getStartTime();
        if (dto.getEndTime() != null) this.endTime = dto.getEndTime();
        if (dto.getMessage() != null) this.message = dto.getMessage();
        this.lastModified = now;
    }
    
    public void extendEndTime() {
        endTime = endTime.nextReservationTime();
    }
    
    public ReservationParticipate attendMember(LocalDateTime now, Supplier<ReservationParticipate> participateSupplier) {
        ReservationParticipate participate = participateSupplier.get();
        participate.editAttendance(isLate(now));
        return participate;
    }
    
    private Boolean isLate(LocalDateTime now) {
        return LocalDateTime.of(date, endTime.localTime).isBefore(now);
    }
}
