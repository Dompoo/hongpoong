package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.InstrumentBorrow;
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
    
    public InstrumentBorrow toDomain() {
        return InstrumentBorrow.builder()
                .id(this.id)
                .borrowDate(this.borrowDate)
                .instrument(this.instrumentJpaEntity.toDomain())
                .member(this.memberJpaEntity.toDomain())
                .reservation(this.reservationJpaEntity.toDomain())
                .build();
    }
    
    public static InstrumentBorrowJpaEntity of(InstrumentBorrow instrumentBorrow) {
        return InstrumentBorrowJpaEntity.builder()
                .id(instrumentBorrow.getId())
                .borrowDate(instrumentBorrow.getBorrowDate())
                .instrumentJpaEntity(InstrumentJpaEntity.of(instrumentBorrow.getInstrument()))
                .memberJpaEntity(MemberJpaEntity.of(instrumentBorrow.getMember()))
                .reservationJpaEntity(ReservationJpaEntity.of(instrumentBorrow.getReservation()))
                .build();
    }
}
