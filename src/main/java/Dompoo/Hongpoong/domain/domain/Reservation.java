package Dompoo.Hongpoong.domain.domain;

import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditDto;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.enums.ReservationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Supplier;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reservation {

    private Long id;
    private LocalDate date;
    private ReservationType type;
    private ReservationTime startTime;
    private ReservationTime endTime;
    private LocalDateTime lastModified;
    private String message;
    private Boolean participationAvailable;
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
    
    public Attendance attendMember(LocalDateTime now, Supplier<Attendance> participateSupplier) {
        Attendance participate = participateSupplier.get();
        participate.editAttendance(isLate(now));
        return participate;
    }
    
    private Boolean isLate(LocalDateTime now) {
        return LocalDateTime.of(date, endTime.localTime).isBefore(now);
    }
}
