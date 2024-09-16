package Dompoo.Hongpoong.domain.jpaEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentBorrowJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate borrowDate;
    
    @ManyToOne @JoinColumn(name = "instrument_id")
    private InstrumentJpaEntity instrumentJpaEntity;
    
    @ManyToOne @JoinColumn(name = "member_id")
    private MemberJpaEntity memberJpaEntity;
    
    @ManyToOne @JoinColumn(name = "reservation_id")
    private ReservationJpaEntity reservationJpaEntity;
}
