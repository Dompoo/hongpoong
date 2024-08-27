package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationSearchRequest;
import Dompoo.Hongpoong.config.WithMockMember;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ReservationControllerTest {

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
        reservationRepository.deleteAll();
    }

    /**
     * 예약 전체 조회 API 테스트 코드
     */

    @Test
    @DisplayName("전체 예약 조회")
    @WithMockMember
    void menu() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        Reservation reservation1 = reservationRepository.save(Reservation.builder()
                .member(member)
                .number(15)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .message("")
                .build());

        Reservation reservation2 = reservationRepository.save(Reservation.builder()
                .member(member)
                .number(13)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .message("")
                .build());

        ReservationSearchRequest request = ReservationSearchRequest.builder()
                .date(LocalDate.of(2025, 12, 20))
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation/search")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(reservation1.getId()))
                .andExpect(jsonPath("$[0].username").value("창근"))
                .andExpect(jsonPath("$[0].number").value(15))
                .andExpect(jsonPath("$[0].date").value("2025-12-20"))
                .andExpect(jsonPath("$[0].startTime").value(12))
                .andExpect(jsonPath("$[0].endTime").value(22))
                .andExpect(jsonPath("$[0].message").value(""))
                .andExpect(jsonPath("$[1].id").value(reservation2.getId()))
                .andExpect(jsonPath("$[1].username").value("창근"))
                .andExpect(jsonPath("$[1].number").value(13))
                .andExpect(jsonPath("$[1].date").value("2025-12-20"))
                .andExpect(jsonPath("$[1].startTime").value(12))
                .andExpect(jsonPath("$[1].endTime").value(22))
                .andExpect(jsonPath("$[1].message").value(""))
                .andDo(print());

    }

    /**
     * 예약 추가 API 테스트 코드
     */

    @Test
    @DisplayName("예약 추가")
    @WithMockMember
    void add() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .number(13)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .message("")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("예약 추가시 날짜는 과거일 수 없다.")
    @WithMockMember
    void addFail3() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .number(13)
                .date(LocalDate.of(2000, 12, 20))
                .startTime(12)
                .endTime(22)
                .message("")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[과거 날짜일 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 추가시 시간은 9시 이상이어야 한다.")
    @WithMockMember
    void addFail4() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .number(13)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(8)
                .endTime(22)
                .message("")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[9시 이상의 시간이어야 합니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 추가시 시간은 22시 이하이어야 한다.")
    @WithMockMember
    void addFail5() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .number(13)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(23)
                .message("")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[22시 이하의 시간이어야 합니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 추가시 시작시간은 종료시간보다 앞서야 한다.")
    @WithMockMember
    void addFail6() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .number(13)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(13)
                .endTime(12)
                .message("")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("시작 시간은 종료 시간보다 앞서야 합니다."))
                .andDo(print());
    }

    /**
     * 예약 상세 조회 API 테스트 코드
     */

    @Test
    @DisplayName("예약 상세 조회")
    @WithMockMember
    void detail() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        //expected
        mockMvc.perform(get("/reservation/{id}", reservation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.username").value("창근"))
                .andExpect(jsonPath("$.date").value("2025-12-20"))
                .andExpect(jsonPath("$.startTime").value(12))
                .andExpect(jsonPath("$.endTime").value(22))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 예약 상세 조회")
    @WithMockMember
    void detailFail() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        //expected
        mockMvc.perform(get("/reservation/{id}", reservation.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 예약입니다."))
                .andDo(print());
    }

    /**
     * 예약 수정 API 테스트 코드
     */

    @Test
    @DisplayName("예약 전체 수정")
    @WithMockMember
    void edit() throws Exception {
        //given
        Member member = memberRepository.findAll().getLast();

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(11)
                .endTime(13)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", reservation.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.startTime").value(11))
                .andExpect(jsonPath("$.endTime").value(13))
                .andExpect(jsonPath("$.date").value("2025-12-15"))

                .andDo(print());
    }

    @Test
    @DisplayName("예약 부분 수정")
    @WithMockMember
    void edit2() throws Exception {
        //given
        Member member = memberRepository.findAll().getLast();

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .startTime(11)
                .endTime(18)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", reservation.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 예약 수정")
    @WithMockMember
    void editFail1() throws Exception {
        //given
        Member member = memberRepository.findAll().getLast();

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(11)
                .endTime(18)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", reservation.getId() + 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("예약 수정시 과거날짜일 수 없다.")
    @WithMockMember
    void editFail2() throws Exception {
        //given
        Member member = memberRepository.findAll().getLast();

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2000, 12, 15))
                .startTime(11)
                .endTime(18)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", reservation.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[과거 날짜일 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 수정시 9시 이상이어야 한다.")
    @WithMockMember
    void editFail3() throws Exception {
        //given
        Member member = memberRepository.findAll().getLast();

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(8)
                .endTime(18)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", reservation.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[9시 이상의 시간이어야 합니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 수정시 22시 이하이어야 한다.")
    @WithMockMember
    void editFail4() throws Exception {
        //given
        Member member = memberRepository.findAll().getLast();

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(11)
                .endTime(23)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", reservation.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[22시 이하의 시간이어야 합니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("내가 한 예약만 수정할 수 있다.")
    @WithMockMember
    void editFail5() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(11)
                .endTime(18)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", reservation.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("수정할 수 없습니다."))
                .andDo(print());
    }

    /**
     * 예약 삭제 API 테스트 코드
     */

    @Test
    @DisplayName("예약 삭제")
    @WithMockMember
    void delete1() throws Exception {
        //given
        Member member = memberRepository.findAll().getLast();

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        //expected
        mockMvc.perform(delete("/reservation/{id}", reservation.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 예약 삭제 실패")
    @WithMockMember
    void deleteFail() throws Exception {
        //given
        Member member = memberRepository.findAll().getLast();

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        //expected
        mockMvc.perform(delete("/reservation/{id}", reservation.getId() + 1))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("내가 한 예약만 삭제할 수 있다.")
    @WithMockMember
    void deleteFail2() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        //expected
        mockMvc.perform(delete("/reservation/{id}", reservation.getId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("삭제할 수 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("관리자 예약 수정")
    @WithMockMember(role = "ROLE_ADMIN")
    void editManage() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("yoonH@naver.com")
                .username("윤호")
                .password("qwer")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(11)
                .endTime(18)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/manage/{id}", reservation.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원은 관리자 예약 수정할 수 없다.")
    @WithMockMember
    void editManageFail() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("yoonH@naver.com")
                .username("윤호")
                .password("qwer")
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .date(LocalDate.of(2025, 12, 20))
                .startTime(12)
                .endTime(22)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(11)
                .endTime(18)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/manage/{id}", reservation.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("[인증오류] 권한이 없습니다."))
                .andDo(print());
    }
}