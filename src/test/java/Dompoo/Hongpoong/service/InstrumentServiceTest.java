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
import Dompoo.Hongpoong.domain.entity.InstrumentBorrow;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.InstrumentType;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.repository.InstrumentBorrowRepository;
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
    @Autowired
    private InstrumentBorrowRepository instrumentBorrowRepository;
    
    private static final LocalDate NOW = LocalDate.now().minusDays(1);
    private static final String NAME = "장구 1";
    private static final Club CLUB1 = HWARANG;
    private static final Club CLUB2 = SANTLE;
    private static final InstrumentType INSTRUMENT_TYPE = JANGGU;
    private static final ReservationTime START_TIME = ReservationTime.TIME_0900;
    private static final ReservationTime END_TIME = ReservationTime.TIME_1500;

    @AfterEach
    void setUp() {
        instrumentBorrowRepository.deleteAllInBatch();
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
                .name(NAME)
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
                Instrument.builder().name("이름 1").club(CLUB1).type(KKWANGGWARI).build(),
                Instrument.builder().name("이름 2").club(CLUB1).type(JANGGU).build(),
                Instrument.builder().name("이름 3").club(CLUB2).type(BUK).build(),
                Instrument.builder().name("이름 4").club(CLUB2).type(SOGO).build(),
                Instrument.builder().name("이름 5").club(CLUB2).type(JING).build())
        );

        //when
        List<InstrumentResponse> response = service.findAllOtherClubInstrument(CLUB1);

        //then
        assertEquals(3, response.size());
        assertEquals("이름 3", response.get(0).getName());
        assertEquals("북", response.get(0).getType());
        assertEquals("이름 4", response.get(1).getName());
        assertEquals("소고", response.get(1).getType());
        assertEquals("이름 5", response.get(2).getName());
        assertEquals("징", response.get(2).getType());
    }

    @Test
    @DisplayName("내 동아리의 악기 조회")
    void findMyList() {
        //given
        instrumentRepository.saveAll(List.of(
                Instrument.builder().name("이름 1").club(CLUB1).type(KKWANGGWARI).build(),
                Instrument.builder().name("이름 2").club(CLUB1).type(JANGGU).build(),
                Instrument.builder().name("이름 3").club(CLUB2).type(BUK).build(),
                Instrument.builder().name("이름 4").club(CLUB2).type(SOGO).build(),
                Instrument.builder().name("이름 5").club(CLUB2).type(JING).build())
        );

        //when
        List<InstrumentResponse> response = service.findAllMyClubInstrument(CLUB1);

        //then
        assertEquals(2, response.size());
        assertEquals("이름 1", response.get(0).getName());
        assertEquals("꽹과리", response.get(0).getType());
        assertEquals("이름 2", response.get(1).getName());
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
                .date(NOW)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());
        
        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .name(NAME)
                .club(HWARANG)
                .available(true)
                .type(KKWANGGWARI)
                .build());

        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .reservationId(reservation.getId())
                .build();

        //when
        service.borrowInstrument(me.getId(), instrument.getId(), request, NOW);

        //then
        assertEquals(false, instrumentRepository.findById(instrument.getId()).get().getAvailable());
        assertEquals(1, instrumentBorrowRepository.count());
        InstrumentBorrow first = instrumentBorrowRepository.findAll().getFirst();
        assertEquals(NOW, first.getBorrowDate());
        assertEquals(instrument.getId(), first.getInstrument().getId());
        assertEquals(me.getId(), first.getMember().getId());
        assertEquals(reservation.getId(), first.getReservation().getId());
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
        InstrumentNotFound e = Assertions.assertThrows(InstrumentNotFound.class, () -> service.borrowInstrument(me.getId(), instrument.getId() + 1, request, NOW));
        
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
        InstrumentNotAvailable e = Assertions.assertThrows(InstrumentNotAvailable.class, () -> service.borrowInstrument(me.getId(), instrument.getId(), request, NOW));
        
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
                .available(false)
                .build());
        
        instrumentBorrowRepository.save(InstrumentBorrow.builder()
                .instrument(instrument)
                .member(me)
                .reservation(reservation)
                .borrowDate(NOW)
                .build());
        
        //when
        service.returnInstrument(me.getId(), instrument.getId());

        //then
        Instrument inst = instrumentRepository.findAll().getFirst();
        assertEquals(inst.getAvailable(), true);
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
        
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(me)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());
        
        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .name(NAME)
                .club(me.getClub())
                .type(KKWANGGWARI)
                .build());
        
        instrumentBorrowRepository.saveAll(List.of(
                InstrumentBorrow.builder().instrument(instrument).member(me).reservation(reservation).build(),
                InstrumentBorrow.builder().instrument(instrument).member(me).reservation(reservation).build()
        ));

        //when
        InstrumentDetailResponse response = service.findInstrumentDetail(instrument.getId());

        //then
        assertEquals(NAME, response.getName());
        assertEquals(2, response.getBorrowHistory().size());
        assertEquals(KKWANGGWARI.korName, response.getType());
        assertEquals(SANTLE.korName, response.getClub());
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
                .name(NAME)
                .type(INSTRUMENT_TYPE)
                .available(false)
                .build();

        //when
        service.editInstrument(me.getClub(), instrument.getId(), request.toDto());

        //then
        Instrument inst = instrumentRepository.findAll().getFirst();
        assertEquals(NAME, inst.getName());
        assertEquals(INSTRUMENT_TYPE.korName, inst.getType().korName);
        assertFalse(inst.getAvailable());
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
                .name(NAME)
                .type(INSTRUMENT_TYPE)
                .available(false)
                .build();
        
        //when
        service.editInstrumentByAdmin(instrument.getId(), request.toDto());
        
        //then
        Instrument inst = instrumentRepository.findAll().getFirst();
        assertEquals(NAME, inst.getName());
        assertEquals(INSTRUMENT_TYPE.korName, inst.getType().korName);
        assertFalse(inst.getAvailable());
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