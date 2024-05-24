package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Instrument;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.repository.InstrumentRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.ReservationRepository;
import Dompoo.Hongpoong.request.Instrument.InstrumentBorrowRequest;
import Dompoo.Hongpoong.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.request.Instrument.InstrumentEditRequest;
import Dompoo.Hongpoong.response.Instrument.InstrumentBorrowResponse;
import Dompoo.Hongpoong.response.Instrument.InstrumentResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static Dompoo.Hongpoong.domain.Instrument.InstrumentType.*;
import static Dompoo.Hongpoong.domain.Member.Club.HWARANG;
import static Dompoo.Hongpoong.domain.Member.Club.SANTLE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InstrumentServiceTest {

    @Autowired
    private InstrumentService service;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        reservationRepository.deleteAll();
        instrumentRepository.deleteAll();
    }

    @Test
    @DisplayName("악기 추가")
    void addOne() {
        //given
        Member me = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        InstrumentCreateRequest request = InstrumentCreateRequest.builder()
                .type(1)
                .build();

        //when
        service.addOne(me.getId(), request);

        //then
        assertEquals(1, instrumentRepository.findAll().size());
    }

    @Test
    @DisplayName("다른 동아리의 악기 조회")
    void findOther() {
        //given
        Member me = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Member other = memberRepository.save(Member.builder()
                .username("강윤호")
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
        List<InstrumentResponse> response = service.getListOther(me.getId());

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
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Member other = memberRepository.save(Member.builder()
                .username("강윤호")
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
        List<InstrumentResponse> response = service.getList(me.getId());

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
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Member other = memberRepository.save(Member.builder()
                .username("강윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(HWARANG)
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(me)
                .number(15)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
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
        InstrumentBorrowResponse response = service.borrowOne(me.getId(), request);

        //then
        assertEquals(instrumentRepository.findAll().getFirst().getReservation().getId(), reservation.getId());
        assertEquals(instrument.getId(), response.getInstrumentId());
        assertEquals(LocalDate.of(2025, 12, 20), response.getReturnDate());
        assertEquals(21, response.getReturnTime());
    }

    @Test
    @DisplayName("악기 반납하기")
    void returnOne() {
        //given
        Member me = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Member other = memberRepository.save(Member.builder()
                .username("강윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(HWARANG)
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(me)
                .number(15)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
                .message("")
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(other)
                .type(KKWANGGWARI)
                .build());

        instrument.setReservation(reservation);

        //when
        service.returnOne(instrument.getId());

        //then
        assertEquals(instrument.getReservation(), null);
        assertEquals(reservation.getInstruments().size(), 0);
    }

    @Test
    @DisplayName("악기 1개 조회")
    void getOne() {
        //given
        Member me = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        //when
        InstrumentResponse response = service.getOne(instrument.getId());

        //then
        assertEquals("꽹과리", response.getType());
        assertEquals("산틀", response.getClub());
    }

    @Test
    @DisplayName("악기 전체 수정")
    void editOne() {
        //given
        Member me = memberRepository.save(Member.builder()
                .username("창근")
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
        service.editOne(me.getId(), instrument.getId(), request);

        //then
        assertEquals("장구", instrumentRepository.findAll().getFirst().getType());
        assertFalse(instrumentRepository.findAll().getFirst().isAvailable());
    }

    @Test
    @DisplayName("악기 일부 수정1")
    void editOne1() {
        //given
        Member me = memberRepository.save(Member.builder()
                .username("창근")
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
        service.editOne(me.getId(), instrument.getId(), request);

        //then
        assertEquals("꽹과리", instrumentRepository.findAll().getFirst().getType());
        assertFalse(instrumentRepository.findAll().getFirst().isAvailable());
    }

    @Test
    @DisplayName("악기 일부 수정2")
    void editOne2() {
        //given
        Member me = memberRepository.save(Member.builder()
                .username("창근")
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
        service.editOne(me.getId(), instrument.getId(), request);

        //then
        assertEquals("장구", instrumentRepository.findAll().getFirst().getType());
        assertTrue(instrumentRepository.findAll().getFirst().isAvailable());
    }

    @Test
    @DisplayName("악기 삭제")
    void deleteOne() {
        //given
        Member me = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        //when
        service.deleteOne(me.getId(), instrument.getId());

        //then
        assertEquals(0, instrumentRepository.findAll().size());
    }
}