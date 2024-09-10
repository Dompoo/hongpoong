package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.reservation.request.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.api.service.ReservationService;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationParticipateRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ReservationServiceTest {

    @Autowired
    private ReservationService service;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReservationParticipateRepository reservationParticipateRepository;
    
    private static final LocalDate DATE = LocalDate.now().plusDays(10);
    private static final LocalDate DATE2 = DATE.plusMonths(1);
    private static final ReservationTime START_TIME = ReservationTime.TIME_0900;
    private static final LocalTime START_TIME_LOCALTIME = START_TIME.localTime;
    private static final ReservationTime END_TIME = ReservationTime.TIME_1500;
    private static final LocalTime END_TIME_LOCALTIME = END_TIME.localTime;
    
    @BeforeEach
    void setUp() {
        reservationParticipateRepository.deleteAllInBatch();
        reservationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("예약 추가")
    void add() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .club(Club.SANTLE)
                .build());

        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(DATE)
                .startTime(START_TIME_LOCALTIME)
                .endTime(END_TIME_LOCALTIME)
                .message("")
                .build();

        //when
        service.createReservation(member.getId(), request);

        //then
        assertEquals(1, reservationRepository.count());
    }

    @Test
    @DisplayName("존재하지 않는 유저가 예약 추가 시도시 실패한다.")
    void addFail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(DATE)
                .startTime(START_TIME_LOCALTIME)
                .endTime(END_TIME_LOCALTIME)
                .message("")
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.createReservation(member.getId() + 1, request));


        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }
    
    @Test
    @DisplayName("예약 추가시 시작시간이 종료시간보다 늦으면 안된다.")
    void addFail2() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(DATE)
                .startTime(ReservationTime.TIME_1400.localTime)
                .endTime(ReservationTime.TIME_0900.localTime)
                .message("")
                .build();
        
        //when
        EndForwardStart e = assertThrows(EndForwardStart.class, () -> service.createReservation(member.getId(), request));
        
        //then
        assertEquals(e.getMessage(), "시작 시간은 종료 시간보다 앞서야 합니다.");
        assertEquals(e.statusCode(), "400");
    }
    
    @Test
    @DisplayName("연도와 달로 예약 조회")
    void yearMonth() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE2)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        //when
        List<ReservationResponse> response = service.findAllReservationOfYearAndMonth(DATE.getYear(), DATE.getMonth().getValue());
        
        //then
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getReservationId(), reservation.getId());
        assertEquals(response.get(0).getCreatorName(), "창근");
        assertEquals(response.get(0).getDate(), DATE);
        assertEquals(response.get(0).getStartTime(), START_TIME_LOCALTIME);
        assertEquals(response.get(0).getEndTime(), END_TIME_LOCALTIME);
    }
    
    @Test
    @DisplayName("날짜로 예약 조회")
    void day() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE2)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        //when
        List<ReservationResponse> response = service.findAllReservationOfDate(DATE);
        
        //then
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getReservationId(), reservation.getId());
        assertEquals(response.get(0).getCreatorName(), "창근");
        assertEquals(response.get(0).getDate(), DATE);
        assertEquals(response.get(0).getStartTime(), START_TIME_LOCALTIME);
        assertEquals(response.get(0).getEndTime(), END_TIME_LOCALTIME);
    }
    
    @Test
    @DisplayName("오늘 해야하는 예약 전체 조회")
    void todo() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE2)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        //when
        List<ReservationResponse> response = service.findAllReservationOfDate(DATE);
        
        //then
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getReservationId(), reservation.getId());
        assertEquals(response.get(0).getCreatorName(), "창근");
        assertEquals(response.get(0).getDate(), DATE);
        assertEquals(response.get(0).getStartTime(), START_TIME_LOCALTIME);
        assertEquals(response.get(0).getEndTime(), END_TIME_LOCALTIME);
    }

    @Test
    @DisplayName("예약 상세 조회")
    void detail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        //when
        ReservationDetailResponse response = service.findReservationDetail(reservation.getId());
        
        //then
        assertEquals(response.getReservationId(), reservation.getId());
        assertEquals(response.getCreatorName(), "창근");
        assertEquals(response.getDate(), DATE);
        assertEquals(response.getStartTime(), START_TIME_LOCALTIME);
        assertEquals(response.getEndTime(), END_TIME_LOCALTIME);
    }

    @Test
    @DisplayName("존재하지 않는 예약 상세 조회")
    void detailFail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class,
                () -> service.findReservationDetail(reservation.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }
    
    @Test
    @DisplayName("예약 시간 연장")
    void extend() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        LocalTime now = END_TIME_LOCALTIME.minusMinutes(25);
        
        //when
        service.extendReservationTime(member.getId(), reservation.getId(), now);
        
        //then
        Reservation editedReservation = reservationRepository.findById(reservation.getId()).get();
        assertEquals(ReservationTime.from(END_TIME.localTime.plusMinutes(30)), editedReservation.getEndTime());
    }
    
    @Test
    @DisplayName("예약 시간 연장은 예약자만 가능하다.")
    void extendFail3() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        Member anotherMember = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        LocalTime now = END_TIME_LOCALTIME.minusMinutes(35);
        
        //when
        EditFailException e = assertThrows(EditFailException.class, () -> service.extendReservationTime(anotherMember.getId(), reservation.getId(), now));
        
        //then
        assertEquals("403", e.statusCode());
        assertEquals("수정할 수 없습니다.", e.getMessage());
    }
    
    @Test
    @DisplayName("예약 시간 연장은 종료 30분 전에만 가능하다.")
    void extendFail1() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        LocalTime now = END_TIME_LOCALTIME.minusMinutes(35);
        
        //when
        TimeExtendNotAvailableException e = assertThrows(TimeExtendNotAvailableException.class, () -> service.extendReservationTime(member.getId(), reservation.getId(), now));
        
        //then
        assertEquals("400", e.statusCode());
        assertEquals("시간 연장은 연습 종료 30분 전에만 가능합니다.", e.getMessage());
    }
    
    @Test
    @DisplayName("예약 시간 연장은 연습 종료 후에 불가능하다.")
    void extendFail2() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        LocalTime now = END_TIME_LOCALTIME.plusMinutes(1);
        
        //when
        TimeExtendNotAvailableException e = assertThrows(TimeExtendNotAvailableException.class, () -> service.extendReservationTime(member.getId(), reservation.getId(), now));
        
        //then
        assertEquals("400", e.statusCode());
        assertEquals("시간 연장은 연습 종료 30분 전에만 가능합니다.", e.getMessage());
    }

    @Test
    @DisplayName("예약 수정")
    void edit() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(START_TIME_LOCALTIME)
                .endTime(END_TIME_LOCALTIME)
                .build();
        
        LocalDateTime now = LocalDateTime.of(2000, 5, 17, 11, 23, 30);

        //when
        service.editReservation(member.getId(), reservation.getId(), request.toDto(), now);

        //then
        Reservation find = assertDoesNotThrow(() -> reservationRepository.findById(reservation.getId())
                .orElseThrow());
        assertEquals(find.getCreator().getId(), member.getId());
        assertEquals(find.getDate(), LocalDate.of(2025, 12, 15));
        assertEquals(find.getStartTime(), START_TIME);
        assertEquals(find.getEndTime(), END_TIME);
    }

    @Test
    @DisplayName("예약 수정 실패")
    void editFail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(START_TIME_LOCALTIME)
                .endTime(END_TIME_LOCALTIME)
                .build();
        
        LocalDateTime now = LocalDateTime.of(2000, 5, 17, 11, 23, 30);

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class, () ->
                service.editReservation(member.getId(), reservation.getId() + 1, request.toDto(), now));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("예약자가 아닌 유저가 예약 수정 시도시 실패")
    void editFai2() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(START_TIME_LOCALTIME)
                .endTime(END_TIME_LOCALTIME)
                .build();
        
        LocalDateTime now = LocalDateTime.of(2000, 5, 17, 11, 23, 30);

        //when
        EditFailException e = assertThrows(EditFailException.class, () ->
                service.editReservation(member.getId() + 1, reservation.getId(), request.toDto(), now));

        //then
        assertEquals(e.getMessage(), "수정할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("예약 삭제")
    void delete() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        //when
        service.deleteReservation(member.getId(), reservation.getId());

        //then
        assertEquals(reservationRepository.count(), 0);
    }

    @Test
    @DisplayName("예약 삭제 실패")
    void deleteFail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class, () ->
                service.deleteReservation(member.getId(), reservation.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("예약자가 아닌 유저가 예약 삭제 시도시 실패")
    void deleteFail2() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        //when
        DeleteFailException e = assertThrows(DeleteFailException.class, () ->
                service.deleteReservation(member.getId() + 1, reservation.getId()));

        //then
        assertEquals(e.getMessage(), "삭제할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("관리자 예약 수정")
    void editByAdmin() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(START_TIME_LOCALTIME)
                .endTime(END_TIME_LOCALTIME)
                .build();
        
        LocalDateTime now = LocalDateTime.of(2000, 5, 17, 11, 23, 30);

        //when
        service.editReservationByAdmin(reservation.getId(), request.toDto(), now);

        //then
        Reservation find = assertDoesNotThrow(() -> reservationRepository.findById(reservation.getId())
                .orElseThrow());
        assertEquals(find.getCreator().getId(), member.getId());
        assertEquals(find.getDate(), LocalDate.of(2025, 12, 15));
        assertEquals(find.getStartTime(), START_TIME);
        assertEquals(find.getEndTime(), END_TIME);
    }
    
    @Test
    @DisplayName("관리자 예약 삭제")
    void deleteByAdmin() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .creator(member)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        //when
        service.deleteReservationByAdmin(reservation.getId());
        
        //then
        assertEquals(reservationRepository.count(), 0);
    }
}