package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationSearchRequest;
import Dompoo.Hongpoong.api.dto.response.resevation.ReservationResponse;
import Dompoo.Hongpoong.config.MyWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest extends MyWebMvcTest {
    
    private static final Long RESERVATION_ID = 1L;
    private static final String USERNAME = "창근";
    private static final String EMAIL = "dompoo@gmail.com";
    private static final int NUMBER = 10;
    private static final LocalDate DATE = LocalDate.of(2040, 5, 17);
    private static final String DATE_STRING = "2040-05-17";
    private static final int START_TIME = 10;
    private static final int END_TIME = 18;
    private static final String MESSAGE = "안녕하세용!";
    
    private static final Long RESERVATION_ID2 = 2L;
    private static final String USERNAME2 = "윤호";
    private static final String EMAIL2 = "yoonH@gmail.com";
    private static final int NUMBER2 = 20;
    private static final LocalDate DATE2 = LocalDate.of(2040, 10, 10);
    private static final String DATE2_STRING = "2040-10-10";
    private static final int START_TIME2 = 14;
    private static final int END_TIME2 = 22;
    private static final String MESSAGE2 = "하이!";
    
    private static final LocalDateTime LAST_MODIFIED = LocalDateTime.of(2030, 10, 10, 10, 30);
    private static final String LAST_MODIFIED_STRING = "2030-10-10T10:30:00";

    /**
     * 예약 전체 조회 API 테스트 코드
     */

    @Test
    @DisplayName("전체 예약 조회")
    void menu() throws Exception {
        //given
        when(reservationService.getAllReservationOfDate(any())).thenReturn(List.of(
                ReservationResponse.builder()
                        .id(RESERVATION_ID)
                        .username(USERNAME)
                        .email(EMAIL)
                        .number(NUMBER)
                        .date(DATE)
                        .startTime(START_TIME)
                        .endTime(END_TIME)
                        .message(MESSAGE)
                        .lastmodified(LAST_MODIFIED)
                        .build(),
                ReservationResponse.builder()
                        .id(RESERVATION_ID2)
                        .username(USERNAME2)
                        .email(EMAIL2)
                        .number(NUMBER2)
                        .date(DATE2)
                        .startTime(START_TIME2)
                        .endTime(END_TIME2)
                        .message(MESSAGE2)
                        .lastmodified(LAST_MODIFIED)
                        .build()
        ));
        
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
                .andExpect(jsonPath("$[0].id").value(RESERVATION_ID))
                .andExpect(jsonPath("$[0].username").value(USERNAME))
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[0].number").value(NUMBER))
                .andExpect(jsonPath("$[0].date").value(DATE_STRING))
                .andExpect(jsonPath("$[0].startTime").value(START_TIME))
                .andExpect(jsonPath("$[0].endTime").value(END_TIME))
                .andExpect(jsonPath("$[0].message").value(MESSAGE))
                .andExpect(jsonPath("$[0].lastmodified").value(LAST_MODIFIED_STRING))
                .andExpect(jsonPath("$[1].id").value(RESERVATION_ID2))
                .andExpect(jsonPath("$[1].username").value(USERNAME2))
                .andExpect(jsonPath("$[1].email").value(EMAIL2))
                .andExpect(jsonPath("$[1].number").value(NUMBER2))
                .andExpect(jsonPath("$[1].date").value(DATE2_STRING))
                .andExpect(jsonPath("$[1].startTime").value(START_TIME2))
                .andExpect(jsonPath("$[1].endTime").value(END_TIME2))
                .andExpect(jsonPath("$[1].message").value(MESSAGE2))
                .andExpect(jsonPath("$[1].lastmodified").value(LAST_MODIFIED_STRING))
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

    /**
     * 예약 상세 조회 API 테스트 코드
     */

    @Test
    @DisplayName("예약 상세 조회")
    void detail() throws Exception {
        //given
        when(reservationService.getReservationDetail(any())).thenReturn(ReservationResponse.builder()
                .id(RESERVATION_ID)
                .username(USERNAME)
                .email(EMAIL)
                .number(NUMBER)
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message(MESSAGE)
                .lastmodified(LAST_MODIFIED)
                .build());

        //expected
        mockMvc.perform(get("/reservation/{id}", RESERVATION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(RESERVATION_ID))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.number").value(NUMBER))
                .andExpect(jsonPath("$.date").value(DATE_STRING))
                .andExpect(jsonPath("$.startTime").value(START_TIME))
                .andExpect(jsonPath("$.endTime").value(END_TIME))
                .andExpect(jsonPath("$.message").value(MESSAGE))
                .andExpect(jsonPath("$.lastmodified").value(LAST_MODIFIED_STRING))
                .andDo(print());
    }

    /**
     * 예약 수정 API 테스트 코드
     */

    @Test
    @DisplayName("예약 전체 수정")
    void edit() throws Exception {
        //given
        when(reservationService.editReservation(any(), any(), any(), any())).thenReturn(ReservationResponse.builder()
                .id(RESERVATION_ID)
                .username(USERNAME)
                .email(EMAIL)
                .number(NUMBER)
                .date(DATE2)
                .startTime(START_TIME2)
                .endTime(END_TIME2)
                .message(MESSAGE)
                .lastmodified(LAST_MODIFIED)
                .build());
        
        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(DATE2)
                .startTime(START_TIME2)
                .endTime(END_TIME2)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", RESERVATION_ID)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(RESERVATION_ID))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.number").value(NUMBER))
                .andExpect(jsonPath("$.date").value(DATE2_STRING))
                .andExpect(jsonPath("$.startTime").value(START_TIME2))
                .andExpect(jsonPath("$.endTime").value(END_TIME2))
                .andExpect(jsonPath("$.message").value(MESSAGE))
                .andExpect(jsonPath("$.lastmodified").value(LAST_MODIFIED_STRING))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 부분 수정")
    void edit2() throws Exception {
        //given
        ReservationEditRequest request = ReservationEditRequest.builder()
                .startTime(11)
                .endTime(18)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", RESERVATION_ID)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("예약 수정시 과거날짜일 수 없다.")
    void editFail2() throws Exception {
        //given
        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2000, 12, 15))
                .startTime(11)
                .endTime(18)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", RESERVATION_ID)
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

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(8)
                .endTime(18)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", RESERVATION_ID)
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
        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(11)
                .endTime(23)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", RESERVATION_ID)
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
        
        //expected
        mockMvc.perform(delete("/reservation/{id}", RESERVATION_ID))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("관리자 예약 수정")
    void editManage() throws Exception {
        //given

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(11)
                .endTime(18)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/manage/{id}", RESERVATION_ID)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }
}