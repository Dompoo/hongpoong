package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.Reservation;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.enums.ReservationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationJpaEntity {

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
    
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "creator_id")
    private MemberJpaEntity creator;
    
    public Reservation toDomain() {
        return Reservation.builder()
                .id(this.id)
                .date(this.date)
                .type(this.type)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .lastModified(this.lastModified)
                .message(this.message)
                .participationAvailable(this.participationAvailable)
                .creator(this.creator.toDomain())
                .build();
    }
    
    public static ReservationJpaEntity of(Reservation reservation) {
        return ReservationJpaEntity.builder()
                .id(reservation.getId())
                .date(reservation.getDate())
                .type(reservation.getType())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .lastModified(reservation.getLastModified())
                .message(reservation.getMessage())
                .participationAvailable(reservation.getParticipationAvailable())
                .creator(MemberJpaEntity.of(reservation.getCreator()))
                .build();
    }
}
