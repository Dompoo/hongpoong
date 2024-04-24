package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.exception.*;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.ReservationRepository;
import Dompoo.Hongpoong.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.request.reservation.ReservationShiftRequest;
import Dompoo.Hongpoong.response.MenuResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService service;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
    }

    @Test
    @DisplayName("예약 리스트 조회")
    void list() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation1 = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        Reservation reservation2 = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        //when
        List<MenuResponse> list = service.getList();

        //then
        assertEquals(2, list.size());
        assertEquals(list.get(0).getId(), reservation1.getId());
        assertEquals(list.get(1).getId(), reservation2.getId());
        assertEquals(list.get(0).getUsername(), reservation1.getMember().getUsername());
        assertEquals(list.get(1).getUsername(), reservation2.getMember().getUsername());
        assertEquals(list.get(0).getDate(), reservation1.getDate());
        assertEquals(list.get(1).getDate(), reservation2.getDate());
        assertEquals(list.get(0).getTime(), reservation1.getTime());
        assertEquals(list.get(1).getTime(), reservation2.getTime());
        assertEquals(list.get(0).getPriority(), reservation1.getPriority());
        assertEquals(list.get(1).getPriority(), reservation2.getPriority());
    }

    @Test
    @DisplayName("예약 추가")
    void add() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .build();

        //when
        MenuResponse response = service.addReservation(member.getId(), request);

        //then
        assertEquals(1, reservationRepository.count());
        assertEquals(response.getId(), reservationRepository.findAll().getFirst().getId());
    }

    @Test
    @DisplayName("예약 추가 실패")
    void addFail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.addReservation(member.getId() + 1, request));


        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("예약 상세")
    void detail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        //when
        MenuResponse response = service.findReservation(reservation.getId());

        //then
        assertEquals(response.getId(), reservation.getId());
        assertEquals(response.getUsername(), "창근");
        assertEquals(response.getDate(), LocalDate.of(2000, 12, 20));
        assertEquals(response.getTime(), 12);
        assertEquals(response.getPriority(), 1);
    }

    @Test
    @DisplayName("예약 상세 실패")
    void detailFail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class,
                () -> service.findReservation(reservation.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("예약 우선순위 변경")
    void shift() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation1 = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());
        Reservation reservation2 = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(2)
                .build());
        Reservation reservation3 = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(3)
                .build());
        Reservation reservation4 = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(4)
                .build());
        Reservation reservation5 = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(5)
                .build());

        ReservationShiftRequest request = ReservationShiftRequest.builder()
                .priority(4)
                .build();

        //when
        service.shiftReservation(member.getId(), reservation2.getId(), request);

        //then
        assertEquals(reservationRepository.findById(reservation1.getId()).get().getPriority(), 1);
        assertEquals(reservationRepository.findById(reservation2.getId()).get().getPriority(), 4);
        assertEquals(reservationRepository.findById(reservation3.getId()).get().getPriority(), 2);
        assertEquals(reservationRepository.findById(reservation4.getId()).get().getPriority(), 3);
        assertEquals(reservationRepository.findById(reservation5.getId()).get().getPriority(), 5);
    }

    @Test
    @DisplayName("존재하지 않는 예약 우선순위 변경 실패")
    void shiftFail1() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(2)
                .build());

        ReservationShiftRequest request = ReservationShiftRequest.builder()
                .priority(4)
                .build();

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class, () ->
                service.shiftReservation(member.getId(), reservation.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("예약 현재의 우선순위보다 앞으로 변경 실패")
    void shiftFail2() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(2)
                .build());

        ReservationShiftRequest request = ReservationShiftRequest.builder()
                .priority(1)
                .build();

        //when
        ReservationAheadShiftFail e = assertThrows(ReservationAheadShiftFail.class, () ->
                service.shiftReservation(member.getId(), reservation.getId(), request));

        //then
        assertEquals(e.getMessage(), "현재 우선순위보다 높습니다.");
        assertEquals(e.statusCode(), "400");
    }

    @Test
    @DisplayName("예약자가 아닌 유저가 예약 우선순위 변경 시도시 실패")
    void shiftFail3() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(2)
                .build());

        ReservationShiftRequest request = ReservationShiftRequest.builder()
                .priority(4)
                .build();

        //when
        EditFailException e = assertThrows(EditFailException.class, () ->
                service.shiftReservation(member.getId() + 1, reservation.getId(), request));

        //then
        assertEquals(e.getMessage(), "수정할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("예약 수정")
    void edit() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2000, 12, 15))
                .time(13)
                .build();

        //when
        service.editReservation(member.getId(), reservation.getId(), request);

        //then
        Reservation find = assertDoesNotThrow(() -> reservationRepository.findById(reservation.getId())
                .orElseThrow());
        assertEquals(find.getMember().getId(), member.getId());
        assertEquals(find.getDate(), LocalDate.of(2000, 12, 15));
        assertEquals(find.getTime(), 13);
        assertEquals(find.getPriority(), 1);
    }

    @Test
    @DisplayName("예약 수정 실패")
    void editFail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2000, 12, 15))
                .time(13)
                .build();

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class, () ->
                service.editReservation(member.getId(), reservation.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("예약자가 아닌 유저가 예약 수정 시도시 실패")
    void editFai2() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2000, 12, 15))
                .time(13)
                .build();

        //when
        EditFailException e = assertThrows(EditFailException.class, () ->
                service.editReservation(member.getId() + 1, reservation.getId(), request));

        //then
        assertEquals(e.getMessage(), "수정할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("예약 삭제")
    void delete() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());


        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
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
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
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
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        //when
        DeleteFailException e = assertThrows(DeleteFailException.class, () ->
                service.deleteReservation(member.getId() + 1, reservation.getId()));

        //then
        assertEquals(e.getMessage(), "삭제할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }
}