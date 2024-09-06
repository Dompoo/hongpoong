package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationSearchRequest;
import Dompoo.Hongpoong.api.dto.response.resevation.ReservationResponse;
import Dompoo.Hongpoong.api.service.ReservationService;
import Dompoo.Hongpoong.common.exception.impl.DeleteFailException;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.common.exception.impl.ReservationNotFound;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.reservation.Reservation;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
                .build());

        Reservation reservation2 = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
                .build());

        ReservationSearchRequest request = ReservationSearchRequest.builder()
                .date(LocalDate.of(2025, 12, 20))
                .build();

        //when
        List<ReservationResponse> list = service.getAllReservationOfDate(request);

        //then
        assertEquals(2, list.size());
        assertEquals(list.get(0).getId(), reservation1.getId());
        assertEquals(list.get(1).getId(), reservation2.getId());
        assertEquals(list.get(0).getUsername(), reservation1.getCreator().getName());
        assertEquals(list.get(1).getUsername(), reservation2.getCreator().getName());
        assertEquals(list.get(0).getDate(), reservation1.getDate());
        assertEquals(list.get(1).getDate(), reservation2.getDate());
        assertEquals(list.get(0).getStartTime(), reservation1.getStartTime());
        assertEquals(list.get(0).getEndTime(), reservation1.getEndTime());
        assertEquals(list.get(1).getStartTime(), reservation2.getStartTime());
        assertEquals(list.get(1).getEndTime(), reservation2.getEndTime());
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
                .number(13)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(13)
                .endTime(17)
                .message("")
                .build();

        //when
        service.addReservation(member.getId(), request);

        //then
        assertEquals(1, reservationRepository.count());
    }

    @Test
    @DisplayName("존재하지 않는 유저가 예약 추가 시도시 실패한다.")
    void addFail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .number(13)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(13)
                .endTime(17)
                .message("")
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
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
                .build());

        //when
        ReservationResponse response = service.findReservation(reservation.getId());

        //then
        assertEquals(response.getId(), reservation.getId());
        assertEquals(response.getUsername(), "창근");
        assertEquals(response.getDate(), LocalDate.of(2025, 12, 20));
        assertEquals(response.getStartTime(), 11);
        assertEquals(response.getEndTime(), 21);
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
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
                .build());

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class,
                () -> service.findReservation(reservation.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
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
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(13)
                .endTime(19)
                .build();
        
        LocalDateTime now = LocalDateTime.of(2000, 5, 17, 11, 23, 30);

        //when
        service.editReservation(member.getId(), reservation.getId(), request.toDto(), now);

        //then
        Reservation find = assertDoesNotThrow(() -> reservationRepository.findById(reservation.getId())
                .orElseThrow());
        assertEquals(find.getCreator().getId(), member.getId());
        assertEquals(find.getDate(), LocalDate.of(2025, 12, 15));
        assertEquals(find.getStartTime(), 13);
        assertEquals(find.getEndTime(), 19);
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
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(13)
                .endTime(19)
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
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(13)
                .endTime(19)
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
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
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
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
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
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
                .build());

        //when
        DeleteFailException e = assertThrows(DeleteFailException.class, () ->
                service.deleteReservation(member.getId() + 1, reservation.getId()));

        //then
        assertEquals(e.getMessage(), "삭제할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("예약 관리자 수정")
    void editAdmin() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(11)
                .endTime(21)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(13)
                .endTime(19)
                .build();
        
        LocalDateTime now = LocalDateTime.of(2000, 5, 17, 11, 23, 30);

        //when
        service.edit(reservation.getId(), request.toDto(), now);

        //then
        Reservation find = assertDoesNotThrow(() -> reservationRepository.findById(reservation.getId())
                .orElseThrow());
        assertEquals(find.getCreator().getId(), member.getId());
        assertEquals(find.getDate(), LocalDate.of(2025, 12, 15));
        assertEquals(find.getStartTime(), 13);
        assertEquals(find.getEndTime(), 19);
    }
}