package Dompoo.Hongpoong.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentBorrow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate borrowDate;
    
    @ManyToOne @JoinColumn(name = "instrument_id")
    private Instrument instrument;
    
    @ManyToOne @JoinColumn(name = "member_id")
    private Member member;
    
    @ManyToOne @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
