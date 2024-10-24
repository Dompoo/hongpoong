package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentEditRequest;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentDetailResponse;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentResponse;
import Dompoo.Hongpoong.api.service.InstrumentService;
import Dompoo.Hongpoong.common.exception.impl.InstrumentNotAvailable;
import Dompoo.Hongpoong.common.exception.impl.InstrumentNotFound;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.InstrumentType;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.jpaEntity.InstrumentBorrowJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.InstrumentJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InstrumentBorrowJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InstrumentJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ReservationJpaRepository;
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
class InstrumentJpaEntityServiceTest {

    @Autowired
    private InstrumentService service;
    @Autowired
    private InstrumentJpaRepository instrumentJpaRepository;
    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private ReservationJpaRepository reservationJpaRepository;
    @Autowired
    private InstrumentBorrowJpaRepository instrumentBorrowJpaRepository;
    
    private static final LocalDate NOW = LocalDate.now().minusDays(1);
    private static final String NAME = "장구 1";
    private static final Club CLUB1 = HWARANG;
    private static final Club CLUB2 = SANTLE;
    private static final InstrumentType INSTRUMENT_TYPE = JANGGU;
    private static final ReservationTime START_TIME = ReservationTime.TIME_0900;
    private static final ReservationTime END_TIME = ReservationTime.TIME_1500;

    @AfterEach
    void setUp() {
        instrumentBorrowJpaRepository.deleteAllInBatch();
        instrumentJpaRepository.deleteAllInBatch();
        reservationJpaRepository.deleteAllInBatch();
        memberJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("악기 추가")
    void createInstrument() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(MemberJpaEntity.builder()
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
        assertEquals(1, instrumentJpaRepository.findAll().size());
    }

    @Test
    @DisplayName("다른 동아리의 악기 조회")
    void findOther() {
        //given
        instrumentJpaRepository.saveAll(List.of(
                InstrumentJpaEntity.builder().name("이름 1").club(CLUB1).type(KKWANGGWARI).build(),
                InstrumentJpaEntity.builder().name("이름 2").club(CLUB1).type(JANGGU).build(),
                InstrumentJpaEntity.builder().name("이름 3").club(CLUB2).type(BUK).build(),
                InstrumentJpaEntity.builder().name("이름 4").club(CLUB2).type(SOGO).build(),
                InstrumentJpaEntity.builder().name("이름 5").club(CLUB2).type(JING).build())
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
        instrumentJpaRepository.saveAll(List.of(
                InstrumentJpaEntity.builder().name("이름 1").club(CLUB1).type(KKWANGGWARI).build(),
                InstrumentJpaEntity.builder().name("이름 2").club(CLUB1).type(JANGGU).build(),
                InstrumentJpaEntity.builder().name("이름 3").club(CLUB2).type(BUK).build(),
                InstrumentJpaEntity.builder().name("이름 4").club(CLUB2).type(SOGO).build(),
                InstrumentJpaEntity.builder().name("이름 5").club(CLUB2).type(JING).build())
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
        MemberJpaEntity me = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(me)
                .date(NOW)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());
        
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.save(InstrumentJpaEntity.builder()
                .name(NAME)
                .club(HWARANG)
                .available(true)
                .type(KKWANGGWARI)
                .build());

        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .reservationId(reservationJpaEntity.getId())
                .build();

        //when
        service.borrowInstrument(me.getId(), instrumentJpaEntity.getId(), request, NOW);

        //then
        assertEquals(false, instrumentJpaRepository.findById(instrumentJpaEntity.getId()).get().getAvailable());
        assertEquals(1, instrumentBorrowJpaRepository.count());
        InstrumentBorrowJpaEntity first = instrumentBorrowJpaRepository.findAll().getFirst();
        assertEquals(NOW, first.getBorrowDate());
        assertEquals(instrumentJpaEntity.getId(), first.getInstrumentJpaEntity().getId());
        assertEquals(me.getId(), first.getMemberJpaEntity().getId());
        assertEquals(reservationJpaEntity.getId(), first.getReservationJpaEntity().getId());
    }
    
    @Test
    @DisplayName("존재하지 않는 악기 빌리기")
    void borrowFail1() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());
        
        MemberJpaEntity other = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(HWARANG)
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(me)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());
        
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.save(InstrumentJpaEntity.builder()
                .club(other.getClub())
                .type(KKWANGGWARI)
                .build());
        
        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .reservationId(reservationJpaEntity.getId())
                .build();
        
        //when
        InstrumentNotFound e = Assertions.assertThrows(InstrumentNotFound.class, () -> service.borrowInstrument(me.getId(), instrumentJpaEntity.getId() + 1, request, NOW));
        
        //then
        assertEquals(e.statusCode(), "404");
        assertEquals(e.getMessage(), "존재하지 않는 악기입니다.");
    }
    
    @Test
    @DisplayName("빌릴 수 없는 악기 빌리기")
    void borrowFail2() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());
        
        MemberJpaEntity other = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(HWARANG)
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(me)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());
        
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.save(InstrumentJpaEntity.builder()
                .club(other.getClub())
                .type(KKWANGGWARI)
                .available(false)
                .build());
        
        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .reservationId(reservationJpaEntity.getId())
                .build();
        
        //when
        InstrumentNotAvailable e = Assertions.assertThrows(InstrumentNotAvailable.class, () -> service.borrowInstrument(me.getId(), instrumentJpaEntity.getId(), request, NOW));
        
        //then
        assertEquals(e.statusCode(), "400");
        assertEquals(e.getMessage(), "빌릴 수 없는 악기입니다.");
    }

    @Test
    @DisplayName("악기 반납하기")
    void withReturn() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        MemberJpaEntity other = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(HWARANG)
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(me)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());
        
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.save(InstrumentJpaEntity.builder()
                .club(other.getClub())
                .type(KKWANGGWARI)
                .available(false)
                .build());
        
        instrumentBorrowJpaRepository.save(InstrumentBorrowJpaEntity.builder()
                .instrumentJpaEntity(instrumentJpaEntity)
                .memberJpaEntity(me)
                .reservationJpaEntity(reservationJpaEntity)
                .borrowDate(NOW)
                .build());
        
        //when
        service.returnInstrument(me.getId(), instrumentJpaEntity.getId());

        //then
        InstrumentJpaEntity inst = instrumentJpaRepository.findAll().getFirst();
        assertEquals(inst.getAvailable(), true);
    }

    @Test
    @DisplayName("악기 1개 조회")
    void findInstrumentDetail() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(me)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build());
        
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.save(InstrumentJpaEntity.builder()
                .name(NAME)
                .club(me.getClub())
                .type(KKWANGGWARI)
                .build());
        
        instrumentBorrowJpaRepository.saveAll(List.of(
                InstrumentBorrowJpaEntity.builder().instrumentJpaEntity(instrumentJpaEntity).memberJpaEntity(me).reservationJpaEntity(reservationJpaEntity).build(),
                InstrumentBorrowJpaEntity.builder().instrumentJpaEntity(instrumentJpaEntity).memberJpaEntity(me).reservationJpaEntity(reservationJpaEntity).build()
        ));

        //when
        InstrumentDetailResponse response = service.findInstrumentDetail(instrumentJpaEntity.getId());

        //then
        assertEquals(NAME, response.getName());
        assertEquals(2, response.getBorrowHistory().size());
        assertEquals(KKWANGGWARI.korName, response.getType());
        assertEquals(SANTLE.korName, response.getClub());
    }

    @Test
    @DisplayName("악기 수정")
    void withEditedInstrument() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.save(InstrumentJpaEntity.builder()
                .club(me.getClub())
                .type(KKWANGGWARI)
                .build());

        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .name(NAME)
                .type(INSTRUMENT_TYPE)
                .available(false)
                .build();

        //when
        service.editInstrument(me.getClub(), instrumentJpaEntity.getId(), request.toDto());

        //then
        InstrumentJpaEntity inst = instrumentJpaRepository.findAll().getFirst();
        assertEquals(NAME, inst.getName());
        assertEquals(INSTRUMENT_TYPE.korName, inst.getType().korName);
        assertFalse(inst.getAvailable());
    }

    @Test
    @DisplayName("악기 삭제")
    void deleteInstrument() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());

        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.save(InstrumentJpaEntity.builder()
                .club(me.getClub())
                .type(KKWANGGWARI)
                .build());

        //when
        service.deleteInstrument(me.getClub(), instrumentJpaEntity.getId());

        //then
        assertEquals(0, instrumentJpaRepository.findAll().size());
    }
    
    @Test
    @DisplayName("어드민 악기 수정")
    void withEditedInstrumentByAdmin() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());
        
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.save(InstrumentJpaEntity.builder()
                .club(me.getClub())
                .type(KKWANGGWARI)
                .build());
        
        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .name(NAME)
                .type(INSTRUMENT_TYPE)
                .available(false)
                .build();
        
        //when
        service.editInstrumentByAdmin(instrumentJpaEntity.getId(), request.toDto());
        
        //then
        InstrumentJpaEntity inst = instrumentJpaRepository.findAll().getFirst();
        assertEquals(NAME, inst.getName());
        assertEquals(INSTRUMENT_TYPE.korName, inst.getType().korName);
        assertFalse(inst.getAvailable());
    }
    
    @Test
    @DisplayName("어드민 악기 삭제")
    void deleteInstrumentByAdmin() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(SANTLE)
                .build());
        
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.save(InstrumentJpaEntity.builder()
                .club(me.getClub())
                .type(KKWANGGWARI)
                .build());
        
        //when
        service.deleteInstrumentByAdmin(instrumentJpaEntity.getId());
        
        //then
        assertEquals(0, instrumentJpaRepository.findAll().size());
    }
}