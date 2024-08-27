package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentEditRequest;
import Dompoo.Hongpoong.config.WithMockMember;
import Dompoo.Hongpoong.domain.Instrument;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.repository.InstrumentRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.ReservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static Dompoo.Hongpoong.domain.enums.Club.HWARANG;
import static Dompoo.Hongpoong.domain.enums.Club.SANTLE;
import static Dompoo.Hongpoong.domain.enums.InstrumentType.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class InstrumentControllerTest {

    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void setUp() {
        instrumentRepository.deleteAllInBatch();
        reservationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("악기 추가")
    @WithMockMember(role = "ROLE_LEADER")
    void addOne() throws Exception {
        //given
        InstrumentCreateRequest request = InstrumentCreateRequest.builder()
                .type(1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/instrument")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("악기 추가시 악기는 비어있을 수 없다.")
    @WithMockMember
    void addOneFail() throws Exception {
        //given
        InstrumentCreateRequest request = InstrumentCreateRequest.builder()
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/instrument")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[악기는 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원은 악기 추가할 수 없다.")
    @WithMockMember
    void addOneFail1() throws Exception {
        //given
        InstrumentCreateRequest request = InstrumentCreateRequest.builder()
                .type(1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/instrument")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("[인증오류] 권한이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("다른 동아리의 악기 조회")
    @WithMockMember
    void listOther() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();
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

        //expected
        mockMvc.perform(get("/instrument"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].type").value("북"))
                .andExpect(jsonPath("$[1].type").value("소고"))
                .andExpect(jsonPath("$[2].type").value("징"))
                .andDo(print());
    }

    @Test
    @DisplayName("내 동아리의 악기 조회")
    @WithMockMember
    void listMe() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();
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

        //expected
        mockMvc.perform(get("/instrument/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].type").value("꽹과리"))
                .andExpect(jsonPath("$[1].type").value("장구"))
                .andDo(print());
    }

    @Test
    @DisplayName("악기 빌리기")
    @WithMockMember
    void borrow() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();
        Member other = memberRepository.save(Member.builder()
                .username("강윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(SANTLE)
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

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/instrument/borrow")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instrumentId").value(instrument.getId()))
                .andExpect(jsonPath("$.returnDate").value("2025-12-20"))
                .andExpect(jsonPath("$.returnTime").value(21))
                .andDo(print());
    }

    @Test
    @DisplayName("내 악기는 빌릴 수 없다.")
    @WithMockMember
    void borrowFail1() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(me)
                .number(15)
                .date(LocalDate.of(2025, 12, 20))
                .message("")
                .startTime(11)
                .endTime(21)
                .build());

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .reservationId(reservation.getId())
                .instrumentId(instrument.getId())
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/instrument/borrow")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("대여할 수 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 예약의 악기 빌리기")
    @WithMockMember
    void borrowFail2() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();
        Member other = memberRepository.save(Member.builder()
                .username("강윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(SANTLE)
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
                .reservationId(reservation.getId() + 1)
                .instrumentId(instrument.getId())
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/instrument/borrow")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 예약입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 악기 빌리기")
    @WithMockMember
    void borrowFail3() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();
        Member other = memberRepository.save(Member.builder()
                .username("강윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(SANTLE)
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
                .instrumentId(instrument.getId() + 1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/instrument/borrow")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 악기입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("악기 반납하기")
    @WithMockMember
    void returnOne() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();
        Member other = memberRepository.save(Member.builder()
                .username("강윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .club(SANTLE)
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

        //expected
        mockMvc.perform(post("/instrument/return/{id}", instrument.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("악기 1개 조회")
    @WithMockMember
    void getOne() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        //expected
        mockMvc.perform(get("/instrument/{id}", instrument.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("꽹과리"))
                .andExpect(jsonPath("$.club").value("산틀"))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 악기 1개 조회")
    @WithMockMember
    void getOneFail1() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        //expected
        mockMvc.perform(get("/instrument/{id}", instrument.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 악기입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("악기 수정")
    @WithMockMember(role = "ROLE_LEADER")
    void editOne() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .available(true)
                .type(1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/instrument/{id}", instrument.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 악기 수정")
    @WithMockMember(role = "ROLE_LEADER")
    void editOneFail() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .available(true)
                .type(1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/instrument/{id}", instrument.getId() + 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 악기입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원은 악기 수정할 수 없다.")
    @WithMockMember
    void editOneFail1() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .available(true)
                .type(1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/instrument/{id}", instrument.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("[인증오류] 권한이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("악기 삭제")
    @WithMockMember(role = "ROLE_LEADER")
    void deleteOne() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        //expected
        mockMvc.perform(delete("/instrument/{id}", instrument.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 악기 삭제")
    @WithMockMember(role = "ROLE_LEADER")
    void deleteOneFail() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        //expected
        mockMvc.perform(delete("/instrument/{id}", instrument.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 악기입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원은 악기 삭제할 수 없다.")
    @WithMockMember
    void deleteOneFail1() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();

        Instrument instrument = instrumentRepository.save(Instrument.builder()
                .member(me)
                .type(KKWANGGWARI)
                .build());

        //expected
        mockMvc.perform(delete("/instrument/{id}", instrument.getId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("[인증오류] 권한이 없습니다."))
                .andDo(print());
    }
}