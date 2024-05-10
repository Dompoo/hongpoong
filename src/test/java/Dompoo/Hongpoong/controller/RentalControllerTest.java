package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.WithMockMember;
import Dompoo.Hongpoong.domain.Instrument;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.repository.InstrumentRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.RentalRepository;
import Dompoo.Hongpoong.repository.ReservationRepository;
import Dompoo.Hongpoong.request.rental.RentalCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class RentalControllerTest {

    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String INSTRUMENT_OWNER_USERNAME = "윤호";
    private static final String INSTRUMENT_OWNER_EMAIL = "yoonH@naver.com";
    private static final String INSTRUMENT_OWNER_PASSWORD = "1234";

    private static final Member.Club CLUB = Member.Club.SANTLE;

    private static final LocalDate DATE = LocalDate.of(2025, 12, 20);
    private static final String DATE_STRING = "2025-12-20";
    private static final int TIME = 13;

    @AfterEach
    void setUp() {
        rentalRepository.deleteAll();
    }

    /**
     * 대여 추가 API 테스트 코드
     */

    @Test
    @DisplayName("대여 추가")
    @WithMockMember
    void addOne() throws Exception {
        //given
        Member reservationMember = memberRepository.findAll().getLast();

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
                .instrumentIds(List.of(instrument.getId()))
                .reservationId(reservation.getId())
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/rental")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //TODO : 대여 추가 실패 테스트 코드 작성

    /**
     * 대여 상세 조회 API 테스트 코드
     */

    @Test
    @DisplayName("대여 상세 조회")
    @WithMockMember
    void getOne() throws Exception {
        //given
        Member reservationMember = memberRepository.findAll().getLast();

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_EMAIL)
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

        //expected
        mockMvc.perform(get("/rental/{id}", rental.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestMember").value("창근"))
                .andExpect(jsonPath("$.date").value(DATE_STRING))
                .andExpect(jsonPath("$.time").value(TIME))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 대여 상세 조회")
    @WithMockMember
    void getOneFail() throws Exception {
        //given
        Member reservationMember = memberRepository.findAll().getLast();

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_EMAIL)
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

        //expected
        mockMvc.perform(get("/rental/{id}", rental.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("존재하지 않는 대여입니다."))
                .andDo(print());
    }

    /**
     * 대여 삭제 API 테스트 코드
     */

    @Test
    @DisplayName("대여 삭제")
    @WithMockMember
    void delete1() throws Exception {
        //given
        Member reservationMember = memberRepository.findAll().getLast();

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_EMAIL)
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

        //expected
        mockMvc.perform(delete("/rental/{id}", rental.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 대여 삭제")
    @WithMockMember
    void deleteFail() throws Exception {
        //given
        Member reservationMember = memberRepository.findAll().getLast();

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_EMAIL)
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

        //expected
        mockMvc.perform(delete("/rental/{id}", rental.getId() + 1))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("관리자 대여 전체 조회")
    @WithMockMember(role = "ROLE_ADMIN")
    void rentalLog() throws Exception {
        Member reservationMember = memberRepository.findAll().getLast();

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_EMAIL)
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

        //expected
        mockMvc.perform(get("/rental/manage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].requestMember").value("창근"))
                .andExpect(jsonPath("$[0].instruments.length()").value(1))
                .andExpect(jsonPath("$[0].date").value(DATE_STRING))
                .andExpect(jsonPath("$[0].time").value(TIME))

                .andDo(print());
    }

    @Test
    @DisplayName("회원은 관리자 대여 전체 조회할 수 없다.")
    @WithMockMember
    void rentalLogFail() throws Exception {
        Member reservationMember = memberRepository.findAll().getLast();

        Member instrumentMember = memberRepository.save(Member.builder()
                .email(INSTRUMENT_OWNER_EMAIL)
                .username(INSTRUMENT_OWNER_EMAIL)
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

        //expected
        mockMvc.perform(get("/rental/manage"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("[인증오류] 권한이 없습니다."))
                .andDo(print());
    }
}