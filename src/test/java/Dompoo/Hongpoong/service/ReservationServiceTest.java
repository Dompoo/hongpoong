package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.exception.ReservationAheadShiftFail;
import Dompoo.Hongpoong.exception.ReservationNotFound;
import Dompoo.Hongpoong.repository.ReservationRepository;
import Dompoo.Hongpoong.request.ReservationCreateRequest;
import Dompoo.Hongpoong.request.ReservationEditRequest;
import Dompoo.Hongpoong.request.ReservationShiftRequest;
import Dompoo.Hongpoong.response.MenuResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService service;
    @Autowired
    private ReservationRepository repository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("예약 리스트 조회")
    void list() {
        //given
        Reservation reservation1 = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        Reservation reservation2 = repository.save(Reservation.builder()
                .member("member1")
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
        assertEquals(list.get(0).getMember(), reservation1.getMember());
        assertEquals(list.get(1).getMember(), reservation2.getMember());
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
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .build();

        //when
        MenuResponse response = service.addReservation(request);

        //then
        assertEquals(1, repository.count());
        assertEquals(response.getId(), repository.findAll().getFirst().getId());
    }

    @Test
    @DisplayName("예약 상세")
    void detail() {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        //when
        MenuResponse response = service.findReservation(reservation.getId());

        //then
        assertEquals(response.getId(), reservation.getId());
        assertEquals(response.getMember(), "member1");
        assertEquals(response.getDate(), LocalDate.of(2000, 12, 20));
        assertEquals(response.getTime(), 12);
        assertEquals(response.getPriority(), 1);
    }

    @Test
    @DisplayName("예약 상세 실패")
    void detailFail() {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
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
        Reservation reservation1 = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());
        Reservation reservation2 = repository.save(Reservation.builder()
                .member("member2")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(2)
                .build());
        Reservation reservation3 = repository.save(Reservation.builder()
                .member("member3")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(3)
                .build());
        Reservation reservation4 = repository.save(Reservation.builder()
                .member("member4")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(4)
                .build());
        Reservation reservation5 = repository.save(Reservation.builder()
                .member("member5")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(5)
                .build());

        ReservationShiftRequest request = ReservationShiftRequest.builder()
                .priority(4)
                .build();

        //when
        service.shiftReservation(reservation2.getId(), request);

        //then
        assertEquals(repository.findById(reservation1.getId()).get().getPriority(), 1);
        assertEquals(repository.findById(reservation2.getId()).get().getPriority(), 4);
        assertEquals(repository.findById(reservation3.getId()).get().getPriority(), 2);
        assertEquals(repository.findById(reservation4.getId()).get().getPriority(), 3);
        assertEquals(repository.findById(reservation5.getId()).get().getPriority(), 5);
    }

    @Test
    @DisplayName("예약 우선순위 변경 실패")
    void shiftFail1() {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member2")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(2)
                .build());

        ReservationShiftRequest request = ReservationShiftRequest.builder()
                .priority(4)
                .build();

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class, () ->
                service.shiftReservation(reservation.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("예약 현재의 우선순위보다 앞으로 변경 실패")
    void shiftFail2() {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member2")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(2)
                .build());

        ReservationShiftRequest request = ReservationShiftRequest.builder()
                .priority(1)
                .build();

        //when
        ReservationAheadShiftFail e = assertThrows(ReservationAheadShiftFail.class, () ->
                service.shiftReservation(reservation.getId(), request));

        //then
        assertEquals(e.getMessage(), "현재 우선순위보다 높습니다.");
        assertEquals(e.statusCode(), "400");
    }

    @Test
    @DisplayName("예약 수정")
    void edit() {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2000, 12, 15))
                .time(13)
                .build();

        //when
        service.editReservation(reservation.getId(), request);

        //then
        Reservation find = repository.findById(reservation.getId())
                .orElseThrow(() -> new RuntimeException("해당 예약이 없습니다."));
        assertEquals(find.getMember(), "member1");
        assertEquals(find.getDate(), LocalDate.of(2000, 12, 15));
        assertEquals(find.getTime(), 13);
        assertEquals(find.getPriority(), 1);
    }

    @Test
    @DisplayName("예약 수정 실패")
    void editFail() {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
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
                service.editReservation(reservation.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("예약 삭제")
    void delete() {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        //when
        service.deleteReservation(reservation.getId());

        //then
        assertEquals(repository.count(), 0);
    }

    @Test
    @DisplayName("예약 삭제 실패")
    void deleteFail() {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class, () ->
                service.deleteReservation(reservation.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }
}