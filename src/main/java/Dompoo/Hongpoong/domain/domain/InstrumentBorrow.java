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

    private Long id;
    private LocalDate borrowDate;
    private Instrument instrument;
    private Member member;
    private Reservation reservation;
}
