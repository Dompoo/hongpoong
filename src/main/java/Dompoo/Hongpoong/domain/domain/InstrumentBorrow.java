package Dompoo.Hongpoong.domain.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstrumentBorrow {

    private final Long id;
    private final LocalDate borrowDate;
    private final Instrument instrument;
    private final Member member;
    private final Reservation reservation;
    
    public InstrumentBorrow of(Instrument instrument, Member borrower, Reservation reservation, LocalDate now) {
        return InstrumentBorrow.builder()
                .instrument(instrument)
                .member(borrower)
                .reservation(reservation)
                .borrowDate(now)
                .build();
    }
}
