package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.Instrument.*;
import Dompoo.Hongpoong.api.service.InstrumentService;
import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.reservation.Reservation;
import Dompoo.Hongpoong.domain.entity.reservation.ReservationTime;
import Dompoo.Hongpoong.domain.repository.InstrumentRepository;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static Dompoo.Hongpoong.domain.enums.Club.HWARANG;
import static Dompoo.Hongpoong.domain.enums.Club.SANTLE;
import static Dompoo.Hongpoong.domain.enums.InstrumentType.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class InstrumentServiceTest {

    @Autowired
    private InstrumentService service;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    
    private static final ReservationTime START_TIME = ReservationTime.TIME_0900;
    private static final ReservationTime END_TIME = ReservationTime.TIME_1500;

    @AfterEach
    void setUp() {
        instrumentRepository.deleteAllInBatch();
        reservationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("악기 추가")
    void createInstrument() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        InstrumentCreateRequest request = InstrumentCreateRequest.builder()
                .type(1)
                .build();

        //when
        service.createInstrument(me.getId(), request);

        //then
        assertEquals(1, instrumentRepository.findAll().size());
    }

    @Test
    @DisplayName("다른 동아리의 악기 조회")
    void findOther() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Member other = memberRepository.save(Member.builder()
                .name("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(HWARANG)
                .build());

        instrumentRepository.saveAll(List.of(
                Instrument.builder().member(me).type(KKWANGGWARI).build(),
                Instrument.builder().member(me).type(JANGGU).build(),
                Instrument.builder().member(other).type(BUK).build(),
                Instrument.builder().member(other).type(SOGO).build(),
                Instrument.builder().member(other).type(JING).build())
        );

        //when
        List<InstrumentResponse> response = service.findAllOtherClubInstrument(me.getId());

        //then
        assertEquals(3, response.size());
        assertEquals("북", response.get(0).getType());
        assertEquals("소고", response.get(1).getType());
        assertEquals("징", response.get(2).getType());
    }

    @Test
    @DisplayName("다른 동아리의 악기 조회")
    void findMyList() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Member other = memberRepository.save(Member.builder()
                .name("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(HWARANG)
                .build());

        instrumentRepository.saveAll(List.of(
                Instrument.builder().member(me).type(KKWANGGWARI).build(),
                Instrument.builder().member(me).type(JANGGU).build(),
                Instrument.builder().member(other).type(BUK).build(),
                Instrument.builder().member(other).type(SOGO).build(),
                Instrument.builder().member(other).type(JING).build())
        );

        //when
        List<InstrumentResponse> response = service.findAllMyClubInstrument(me.getId());

        //then
        assertEquals(2, response.size());
        assertEquals("꽹과리", response.get(0).getType());
        assertEquals("장구", response.get(1).getType());
    }

    @Test
    @DisplayName("악기 빌리기")
    void borrow() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Member other = memberRepository.save(Member.builder()
                .name("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(HWARANG)
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(me)
                .number(15)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(other)
                .type(KKWANGGWARI)
                .build());

        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .reservationId(reservation.getId())
                .instrumentId(instrument.getId())
                .build();

        //when
        InstrumentBorrowResponse response = service.borrowInstrument(me.getId(), request);

        //then
        assertEquals(instrumentRepository.findAll().getFirst().getReservation().getId(), reservation.getId());
        assertEquals(instrument.getId(), response.getInstrumentId());
        assertEquals(LocalDate.of(2025, 12, 20), response.getReturnDate());
        assertEquals(END_TIME.localTime, response.getReturnTime());
    }

    @Test
    @DisplayName("악기 반납하기")
    void returnInstrument() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Member other = memberRepository.save(Member.builder()
                .name("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(HWARANG)
                .build());
        
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(me)
                .number(15)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(other)
                .type(KKWANGGWARI)
                .build());

        instrument.setReservation(reservation);

        //when
        service.returnInstrument(instrument.getId());

        //then
        Instrument inst = instrumentRepository.findAll().getFirst();
        assertEquals(inst.isAvailable(), true);
        assertEquals(inst.getReservation(), null);
    }

    @Test
    @DisplayName("악기 1개 조회")
    void findInstrumentDetail() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        //when
        InstrumentResponse response = service.findInstrumentDetail(instrument.getId());

        //then
        assertEquals("꽹과리", response.getType());
        assertEquals("산틀", response.getClub());
    }

    @Test
    @DisplayName("악기 전체 수정")
    void editInstrument() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .type(1)
                .available(false)
                .build();

        //when
        service.editInstrument(me.getId(), instrument.getId(), request.toDto());

        //then
        assertEquals("꽹과리", instrumentRepository.findAll().getFirst().getType().korName);
        assertFalse(instrumentRepository.findAll().getFirst().isAvailable());
    }

    @Test
    @DisplayName("악기 일부 수정1")
    void editInstrument1() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .available(false)
                .build();

        //when
        service.editInstrument(me.getId(), instrument.getId(), request.toDto());

        //then
        assertEquals("꽹과리", instrumentRepository.findAll().getFirst().getType().korName);
        assertFalse(instrumentRepository.findAll().getFirst().isAvailable());
    }

    @Test
    @DisplayName("악기 일부 수정2")
    void editInstrument2() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .type(1)
                .build();

        //when
        service.editInstrument(me.getId(), instrument.getId(), request.toDto());

        //then
        assertEquals("꽹과리", instrumentRepository.findAll().getFirst().getType().korName);
        assertTrue(instrumentRepository.findAll().getFirst().isAvailable());
    }

    @Test
    @DisplayName("악기 삭제")
    void deleteInstrument() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        //when
        service.deleteInstrument(me.getId(), instrument.getId());

        //then
        assertEquals(0, instrumentRepository.findAll().size());
    }
}