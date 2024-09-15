package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentEditRequest;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentDetailResponse;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentResponse;
import Dompoo.Hongpoong.api.service.InstrumentService;
import Dompoo.Hongpoong.common.exception.impl.InstrumentNotAvailable;
import Dompoo.Hongpoong.common.exception.impl.InstrumentNotFound;
import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.InstrumentType;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.repository.InstrumentRepository;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    
    private static final Club CLUB1 = HWARANG;
    private static final Club CLUB2 = SANTLE;
    private static final InstrumentType INSTRUMENT_TYPE = JANGGU;
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
                .type(INSTRUMENT_TYPE)
                .build();

        //when
        service.createInstrument(me.getClub(), request);

        //then
        assertEquals(1, instrumentRepository.findAll().size());
    }

    @Test
    @DisplayName("다른 동아리의 악기 조회")
    void findOther() {
        //given
        instrumentRepository.saveAll(List.of(
                Instrument.builder().club(CLUB1).type(KKWANGGWARI).build(),
                Instrument.builder().club(CLUB1).type(JANGGU).build(),
                Instrument.builder().club(CLUB2).type(BUK).build(),
                Instrument.builder().club(CLUB2).type(SOGO).build(),
                Instrument.builder().club(CLUB2).type(JING).build())
        );

        //when
        List<InstrumentResponse> response = service.findAllOtherClubInstrument(CLUB1);

        //then
        assertEquals(3, response.size());
        assertEquals("북", response.get(0).getType());
        assertEquals("소고", response.get(1).getType());
        assertEquals("징", response.get(2).getType());
    }

    @Test
    @DisplayName("내 동아리의 악기 조회")
    void findMyList() {
        //given
        instrumentRepository.saveAll(List.of(
                Instrument.builder().club(CLUB1).type(KKWANGGWARI).build(),
                Instrument.builder().club(CLUB1).type(JANGGU).build(),
                Instrument.builder().club(CLUB2).type(BUK).build(),
                Instrument.builder().club(CLUB2).type(SOGO).build(),
                Instrument.builder().club(CLUB2).type(JING).build())
        );

        //when
        List<InstrumentResponse> response = service.findAllMyClubInstrument(CLUB1);

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

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(me)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());
        
        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .club(HWARANG)
                .available(true)
                .type(KKWANGGWARI)
                .build());

        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .reservationId(reservation.getId())
                .build();

        //when
        InstrumentDetailResponse response = service.borrowInstrument(me.getId(), instrument.getId(), request);

        //then
        assertEquals(instrumentRepository.findById(instrument.getId()).get().getReservation().getId(), reservation.getId());
        assertEquals(instrumentRepository.findById(instrument.getId()).get().getAvailable(), false);
        assertEquals(instrumentRepository.findById(instrument.getId()).get().getBorrower().getId(), me.getId());
        assertEquals(instrument.getId(), response.getInstrumentId());
        assertEquals(LocalDate.of(2025, 12, 20), response.getReturnDate());
        assertEquals(END_TIME.localTime, response.getReturnTime());
    }
    
    @Test
    @DisplayName("존재하지 않는 악기 빌리기")
    void borrowFail1() {
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
                .date(LocalDate.of(2025, 12, 20))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());
        
        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .club(other.getClub())
                .type(KKWANGGWARI)
                .build());
        
        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .reservationId(reservation.getId())
                .build();
        
        //when
        InstrumentNotFound e = Assertions.assertThrows(InstrumentNotFound.class, () -> service.borrowInstrument(me.getId(), instrument.getId() + 1, request));
        
        //then
        assertEquals(e.statusCode(), "404");
        assertEquals(e.getMessage(), "존재하지 않는 악기입니다.");
    }
    
    @Test
    @DisplayName("빌릴 수 없는 악기 빌리기")
    void borrowFail2() {
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
                .date(LocalDate.of(2025, 12, 20))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());
        
        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .club(other.getClub())
                .type(KKWANGGWARI)
                .available(false)
                .build());
        
        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .reservationId(reservation.getId())
                .build();
        
        //when
        InstrumentNotAvailable e = Assertions.assertThrows(InstrumentNotAvailable.class, () -> service.borrowInstrument(me.getId(), instrument.getId(), request));
        
        //then
        assertEquals(e.statusCode(), "400");
        assertEquals(e.getMessage(), "빌릴 수 없는 악기입니다.");
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
                .date(LocalDate.of(2025, 12, 20))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());
        
        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .club(other.getClub())
                .type(KKWANGGWARI)
                .reservation(reservation)
                .build());
        
        //when
        service.returnInstrument(instrument.getId());

        //then
        Instrument inst = instrumentRepository.findAll().getFirst();
        assertEquals(inst.getAvailable(), true);
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
                .club(me.getClub())
                .type(KKWANGGWARI)
                .build());

        //when
        InstrumentDetailResponse response = service.findInstrumentDetail(instrument.getId());

        //then
        assertEquals("꽹과리", response.getType());
        assertEquals("산틀", response.getClub());
    }

    @Test
    @DisplayName("악기 수정")
    void editInstrument() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .club(me.getClub())
                .type(KKWANGGWARI)
                .build());

        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .type(INSTRUMENT_TYPE)
                .available(false)
                .build();

        //when
        service.editInstrument(me.getClub(), instrument.getId(), request.toDto());

        //then
        assertEquals(INSTRUMENT_TYPE.korName, instrumentRepository.findAll().getFirst().getType().korName);
        assertFalse(instrumentRepository.findAll().getFirst().getAvailable());
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
                .club(me.getClub())
                .type(KKWANGGWARI)
                .build());

        //when
        service.deleteInstrument(me.getClub(), instrument.getId());

        //then
        assertEquals(0, instrumentRepository.findAll().size());
    }
    
    @Test
    @DisplayName("어드민 악기 수정")
    void editInstrumentByAdmin() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());
        
        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .club(me.getClub())
                .type(KKWANGGWARI)
                .build());
        
        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .type(INSTRUMENT_TYPE)
                .available(false)
                .build();
        
        //when
        service.editInstrumentByAdmin(instrument.getId(), request.toDto());
        
        //then
        assertEquals(INSTRUMENT_TYPE.korName, instrumentRepository.findAll().getFirst().getType().korName);
        assertFalse(instrumentRepository.findAll().getFirst().getAvailable());
    }
    
    @Test
    @DisplayName("어드민 악기 삭제")
    void deleteInstrumentByAdmin() {
        //given
        Member me = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());
        
        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .club(me.getClub())
                .type(KKWANGGWARI)
                .build());
        
        //when
        service.deleteInstrumentByAdmin(instrument.getId());
        
        //then
        assertEquals(0, instrumentRepository.findAll().size());
    }
}