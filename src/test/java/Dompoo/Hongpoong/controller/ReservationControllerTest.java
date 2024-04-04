package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.repository.ReservationRepository;
import Dompoo.Hongpoong.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.request.reservation.ReservationShiftRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReservationControllerTest {

    @Autowired
    private ReservationRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    /**
     * 예약 전체 조회 API 테스트 코드
     */

    @Test
    @DisplayName("전체 예약 조회")
    void menu() throws Exception {
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
                .time(13)
                .priority(1)
                .build());

        //expected
        mockMvc.perform(get("/reservation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(reservation1.getId()))
                .andExpect(jsonPath("$[0].member").value("member1"))
                .andExpect(jsonPath("$[0].date").value("2000-12-20"))
                .andExpect(jsonPath("$[0].time").value(12))
                .andExpect(jsonPath("$[0].priority").value(1))
                .andExpect(jsonPath("$[1].id").value(reservation2.getId()))
                .andExpect(jsonPath("$[1].member").value("member2"))
                .andExpect(jsonPath("$[1].date").value("2000-12-20"))
                .andExpect(jsonPath("$[1].time").value(13))
                .andExpect(jsonPath("$[1].priority").value(1))
                .andDo(print());
    }

    /**
     * 예약 추가 API 테스트 코드
     */

    @Test
    @DisplayName("예약 추가")
    void add() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .member("member1")
                .date(LocalDate.of(2025, 12, 20))
                .time(12)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member").value("member1"))
                .andExpect(jsonPath("$.date").value("2025-12-20"))
                .andExpect(jsonPath("$.time").value(12))
                .andExpect(jsonPath("$.priority").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 추가시 예약자는 비어있을 수 없다.")
    void addFail1() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(LocalDate.of(2025, 12, 20))
                .time(12)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[예약자는 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 추가시 예약자는 공백일 수 없다.")
    void addFail2() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .member("")
                .date(LocalDate.of(2025, 12, 20))
                .time(12)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[예약자는 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 추가시 날짜는 과거일 수 없다.")
    void addFail3() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
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
    void addFail4() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .member("member1")
                .date(LocalDate.of(2025, 12, 20))
                .time(8)
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
    void addFail5() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .member("member1")
                .date(LocalDate.of(2025, 12, 20))
                .time(23)
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

    /**
     * 예약 상세 조회 API 테스트 코드
     */

    @Test
    @DisplayName("예약 상세 조회")
    void detail() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        //expected
        mockMvc.perform(get("/reservation/{id}", reservation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.member").value("member1"))
                .andExpect(jsonPath("$.date").value("2000-12-20"))
                .andExpect(jsonPath("$.time").value(12))
                .andExpect(jsonPath("$.priority").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 예약 상세 조회")
    void detailFail() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        //expected
        mockMvc.perform(get("/reservation/{id}", reservation.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 예약입니다."))
                .andDo(print());
    }

    /**
     * 예약 우서눈위 변경 API 테스트 코드
     */

    @Test
    @DisplayName("예약 우선순위 변경")
    void shift1() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member2")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationShiftRequest request = ReservationShiftRequest.builder()
                .priority(4)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation/{id}", reservation.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 예약 우선순위 변경")
    void shiftFail1() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member2")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationShiftRequest request = ReservationShiftRequest.builder()
                .priority(4)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation/{id}", reservation.getId() + 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("우선순위를 앞으로 변경할 수 없다.")
    void shiftFail2() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member2")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(3)
                .build());

        ReservationShiftRequest request = ReservationShiftRequest.builder()
                .priority(1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation/{id}", reservation.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("우선순위는 최대 1이어야 한다.")
    void shiftFail3() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member2")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(3)
                .build());

        ReservationShiftRequest request = ReservationShiftRequest.builder()
                .priority(0)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/reservation/{id}", reservation.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[우선순위는 최대 1입니다.]"))
                .andDo(print());
    }

    /**
     * 예약 수정 API 테스트 코드
     */

    @Test
    @DisplayName("예약 전체 수정")
    void edit() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2025, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .time(13)
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
    @DisplayName("예약 부분 수정")
    void edit2() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .time(13)
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
    void editFail1() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2025, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .time(13)
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
    void editFail2() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2025, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2000, 12, 15))
                .time(13)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", reservation.getId() + 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[과거 날짜일 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 수정시 9시 이상이어야 한다.")
    void editFail3() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2025, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .time(8)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", reservation.getId() + 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[9시 이상의 시간이어야 합니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 수정시 22시 이하이어야 한다.")
    void editFail4() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2025, 12, 20))
                .time(12)
                .priority(1)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .time(23)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", reservation.getId() + 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[22시 이하의 시간이어야 합니다.]"))
                .andDo(print());
    }

    /**
     * 예약 삭제 API 테스트 코드
     */

    @Test
    @DisplayName("예약 삭제")
    void delete1() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        //expected
        mockMvc.perform(delete("/reservation/{id}", reservation.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("예약 삭제 실패")
    void deleteFail() throws Exception {
        //given
        Reservation reservation = repository.save(Reservation.builder()
                .member("member1")
                .date(LocalDate.of(2000, 12, 20))
                .time(12)
                .priority(1)
                .build());

        //expected
        mockMvc.perform(delete("/reservation/{id}", reservation.getId() + 1))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}