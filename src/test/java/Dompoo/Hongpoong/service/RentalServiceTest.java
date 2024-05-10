package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Instrument;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.exception.DeleteFailException;
import Dompoo.Hongpoong.exception.RentalNotFound;
import Dompoo.Hongpoong.repository.InstrumentRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.RentalRepository;
import Dompoo.Hongpoong.repository.ReservationRepository;
import Dompoo.Hongpoong.request.rental.RentalCreateRequest;
import Dompoo.Hongpoong.response.rental.RentalResponse;
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
class RentalServiceTest {

    @Autowired
    private RentalService service;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    private static final String INSTRUMENT_OWNER_EMAIL = "dompoo@gmail.com";
    private static final String INSTRUMENT_OWNER_USERNAME = "창근";
    private static final String INSTRUMENT_OWNER_PASSWORD = "1234";

    private static final String RESERVATION_MEMBER_EMAIL = "yoonH@naver.com";
    private static final String RESERVATION_MEMBER_USERNAME = "윤호";
    private static final String RESERVATION_MEMBER_PASSWORD = "qwer";

    private static final Member.Club CLUB = Member.Club.SANTLE;

    private static final LocalDate DATE = LocalDate.of(2025, 12, 20);
    private static final int TIME = 13;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        reservationRepository.deleteAll();
        instrumentRepository.deleteAll();
        rentalRepository.deleteAll();
    }

    @Test
    @DisplayName("대여 추가")
    void addOne() {
        //given
        Member reservationMember = memberRepository.save(Member.builder()
                .email(RESERVATION_MEMBER_EMAIL)
                .username(RESERVATION_MEMBER_USERNAME)
                .password(RESERVATION_MEMBER_PASSWORD)
                .club(CLUB)
                .build());

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_USERNAME)
                .password(INSTRUMENT_OWNER_PASSWORD)
                .club(CLUB)
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(reservationMember)
                .priority(1)
                .date(DATE)
                .time(TIME)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .product("장구")
                .member(instrumentMember)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .reservationId(reservation.getId())
                .instrumentIds(List.of(instrument.getId()))
                .build();

        //when
        service.addRental(reservationMember.getId(), request);

        //then
        assertEquals(rentalRepository.count(), 1);
        assertEquals(rentalRepository.findAll().getFirst().getInstruments().size(), 1);
        assertEquals(rentalRepository.findAll().getFirst().getReservation().getMember().getId(), reservationMember.getId());
        assertEquals(rentalRepository.findAll().getFirst().getReservation().getId(), reservation.getId());
    }

    @Test
    @DisplayName("대여 상세 조회")
    void getOne() {
        //given
        Member reservationMember = memberRepository.save(Member.builder()
                .email(RESERVATION_MEMBER_EMAIL)
                .username(RESERVATION_MEMBER_USERNAME)
                .password(RESERVATION_MEMBER_PASSWORD)
                .club(CLUB)
                .build());

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_USERNAME)
                .password(INSTRUMENT_OWNER_PASSWORD)
                .club(CLUB)
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(reservationMember)
                .priority(1)
                .date(DATE)
                .time(TIME)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .product("장구")
                .member(instrumentMember)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .reservation(reservation)
                .build());
        instrument.setRental(rental);

        //when
        RentalResponse response = service.getDetail(rental.getId());

        //then
        assertEquals(response.getId(), rental.getId());
        assertEquals(response.getRequestMember(), RESERVATION_MEMBER_USERNAME);
        assertEquals(response.getInstruments().getFirst().getId(), instrument.getId());
        assertEquals(response.getDate(), DATE);
        assertEquals(response.getTime(), TIME);
    }

    @Test
    @DisplayName("존재하지 않는 대여 상세 조회")
    void getOneFail() {
        Member reservationMember = memberRepository.save(Member.builder()
                .email(RESERVATION_MEMBER_EMAIL)
                .username(RESERVATION_MEMBER_USERNAME)
                .password(RESERVATION_MEMBER_PASSWORD)
                .club(CLUB)
                .build());

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_USERNAME)
                .password(INSTRUMENT_OWNER_PASSWORD)
                .club(CLUB)
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(reservationMember)
                .priority(1)
                .date(DATE)
                .time(TIME)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .product("장구")
                .member(instrumentMember)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .reservation(reservation)
                .build());
        instrument.setRental(rental);

        //when
        RentalNotFound e = assertThrows(RentalNotFound.class,
                () -> service.getDetail(rental.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 대여입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("대여 삭제")
    void delete() {
        //given
        Member reservationMember = memberRepository.save(Member.builder()
                .email(RESERVATION_MEMBER_EMAIL)
                .username(RESERVATION_MEMBER_USERNAME)
                .password(RESERVATION_MEMBER_PASSWORD)
                .club(CLUB)
                .build());

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_USERNAME)
                .password(INSTRUMENT_OWNER_PASSWORD)
                .club(CLUB)
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(reservationMember)
                .priority(1)
                .date(DATE)
                .time(TIME)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .product("장구")
                .member(instrumentMember)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .reservation(reservation)
                .build());
        instrument.setRental(rental);

        //when
        service.deleteRental(reservationMember.getId(), rental.getId());

        //then
        assertEquals(rentalRepository.count(), 0);
    }

    @Test
    @DisplayName("존재하지 않는 대여 삭제")
    void deleteFail1() {
        Member reservationMember = memberRepository.save(Member.builder()
                .email(RESERVATION_MEMBER_EMAIL)
                .username(RESERVATION_MEMBER_USERNAME)
                .password(RESERVATION_MEMBER_PASSWORD)
                .club(CLUB)
                .build());

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_USERNAME)
                .password(INSTRUMENT_OWNER_PASSWORD)
                .club(CLUB)
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(reservationMember)
                .priority(1)
                .date(DATE)
                .time(TIME)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .product("장구")
                .member(instrumentMember)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .reservation(reservation)
                .build());
        instrument.setRental(rental);

        //when
        RentalNotFound e = assertThrows(RentalNotFound.class, () ->
                service.deleteRental(reservationMember.getId(), rental.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 대여입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("존재하지 않는 유저가 대여 삭제 시도")
    void deleteFail2() {
        Member reservationMember = memberRepository.save(Member.builder()
                .email(RESERVATION_MEMBER_EMAIL)
                .username(RESERVATION_MEMBER_USERNAME)
                .password(RESERVATION_MEMBER_PASSWORD)
                .club(CLUB)
                .build());

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_USERNAME)
                .password(INSTRUMENT_OWNER_PASSWORD)
                .club(CLUB)
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(reservationMember)
                .priority(1)
                .date(DATE)
                .time(TIME)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .product("장구")
                .member(instrumentMember)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .reservation(reservation)
                .build());
        instrument.setRental(rental);

        //when
        DeleteFailException e = assertThrows(DeleteFailException.class, () ->
                service.deleteRental(reservationMember.getId() + 1, rental.getId()));

        //then
        assertEquals(e.getMessage(), "삭제할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("대여자가 아닌 유저가 대여 삭제 시도시 실패")
    void deleteFail3() {
        Member anonymous = memberRepository.save(Member.builder()
                .email("anonymous@gmail.com")
                .username("anonymous")
                .password("1234")
                .club(CLUB)
                .build());

        Member reservationMember = memberRepository.save(Member.builder()
                .email(RESERVATION_MEMBER_EMAIL)
                .username(RESERVATION_MEMBER_USERNAME)
                .password(RESERVATION_MEMBER_PASSWORD)
                .club(CLUB)
                .build());

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_USERNAME)
                .password(INSTRUMENT_OWNER_PASSWORD)
                .club(CLUB)
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(reservationMember)
                .priority(1)
                .date(DATE)
                .time(TIME)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .product("장구")
                .member(instrumentMember)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .reservation(reservation)
                .build());
        instrument.setRental(rental);

        //when
        DeleteFailException e = assertThrows(DeleteFailException.class, () ->
                service.deleteRental(anonymous.getId(), rental.getId()));

        //then
        assertEquals(e.getMessage(), "삭제할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("대여 로그 조회")
    void log() {
        //given
        Member reservationMember = memberRepository.save(Member.builder()
                .email(RESERVATION_MEMBER_EMAIL)
                .username(RESERVATION_MEMBER_USERNAME)
                .password(RESERVATION_MEMBER_PASSWORD)
                .club(CLUB)
                .build());

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_USERNAME)
                .password(INSTRUMENT_OWNER_PASSWORD)
                .club(CLUB)
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(reservationMember)
                .priority(1)
                .date(DATE)
                .time(TIME)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .product("장구")
                .member(instrumentMember)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .reservation(reservation)
                .build());
        instrument.setRental(rental);

        //when
        List<RentalResponse> list = service.getLog();

        //then
        assertEquals(list.size(), 1);
        assertEquals(list.getFirst().getId(), rental.getId());
        assertEquals(list.getFirst().getDate(), DATE);
        assertEquals(list.getFirst().getTime(), TIME);
        assertEquals(list.getFirst().getRequestMember(), RESERVATION_MEMBER_USERNAME);
        assertEquals(list.getFirst().getInstruments().getFirst().getId(), instrument.getId());
    }
}