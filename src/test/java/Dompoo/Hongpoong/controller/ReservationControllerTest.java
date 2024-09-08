package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.config.MyWebMvcTest;
import Dompoo.Hongpoong.domain.entity.reservation.ReservationTime;
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
    private static final String NAME = "창근";
    private static final String EMAIL = "dompoo@gmail.com";
    private static final int NUMBER = 10;
    private static final LocalDate DATE = LocalDate.of(2040, 5, 17);
    private static final String DATE_STRING = "2040-05-17";
    private static final ReservationTime START_TIME = ReservationTime.TIME_0900;
    private static final ReservationTime END_TIME = ReservationTime.TIME_1130;
    private static final String MESSAGE = "안녕하세용!";
    
    private static final Long RESERVATION_ID2 = 2L;
    private static final String USERNAME2 = "윤호";
    private static final String EMAIL2 = "yoonH@gmail.com";
    private static final int NUMBER2 = 20;
    private static final LocalDate DATE2 = LocalDate.of(2040, 10, 10);
    private static final String DATE2_STRING = "2040-10-10";
    private static final ReservationTime START_TIME2 = ReservationTime.TIME_0900;
    private static final ReservationTime END_TIME2 = ReservationTime.TIME_1130;
    private static final String MESSAGE2 = "하이!";
    
    private static final LocalDateTime LAST_MODIFIED = LocalDateTime.of(2030, 10, 10, 10, 30);
    private static final String LAST_MODIFIED_STRING = "2030-10-10T10:30:00";
    
    private static final List<Long> PARTICIPATER_IDS = List.of(1L, 2L, 3L);

    /**
     * 예약 전체 조회 API 테스트 코드
     */
    

    /**
     * 예약 추가 API 테스트 코드
     */

    @Test
    @DisplayName("예약 추가")
    void add() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .number(NUMBER)
                .participaterIds(PARTICIPATER_IDS)
                .date(DATE)
                .startTime(START_TIME.strValue)
                .endTime(END_TIME.strValue)
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
                .number(NUMBER)
                .participaterIds(PARTICIPATER_IDS)
                .date(LocalDate.now().minusDays(1))
                .startTime(START_TIME.strValue)
                .endTime(END_TIME.strValue)
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

    /**
     * 예약 상세 조회 API 테스트 코드
     */

    @Test
    @DisplayName("예약 상세 조회")
    void detail() throws Exception {
        //given
        when(reservationService.findReservationDetail(any())).thenReturn(ReservationResponse.builder()
                .id(RESERVATION_ID)
                .username(NAME)
                .email(EMAIL)
                .number(NUMBER)
                .date(DATE)
                .startTime(START_TIME.strValue)
                .endTime(END_TIME.strValue)
                .message(MESSAGE)
                .lastmodified(LAST_MODIFIED)
                .build());

        //expected
        mockMvc.perform(get("/reservation/{id}", RESERVATION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(RESERVATION_ID))
                .andExpect(jsonPath("$.username").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.number").value(NUMBER))
                .andExpect(jsonPath("$.date").value(DATE_STRING))
                .andExpect(jsonPath("$.startTime").value(START_TIME.strValue))
                .andExpect(jsonPath("$.endTime").value(END_TIME.strValue))
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
                .username(NAME)
                .email(EMAIL)
                .number(NUMBER)
                .date(DATE2)
                .startTime(START_TIME2.strValue)
                .endTime(END_TIME2.strValue)
                .message(MESSAGE)
                .lastmodified(LAST_MODIFIED)
                .build());
        
        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(DATE2)
                .startTime(START_TIME2.strValue)
                .endTime(END_TIME2.strValue)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/{id}", RESERVATION_ID)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(RESERVATION_ID))
                .andExpect(jsonPath("$.username").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.number").value(NUMBER))
                .andExpect(jsonPath("$.date").value(DATE2_STRING))
                .andExpect(jsonPath("$.startTime").value(START_TIME2.strValue))
                .andExpect(jsonPath("$.endTime").value(END_TIME2.strValue))
                .andExpect(jsonPath("$.message").value(MESSAGE))
                .andExpect(jsonPath("$.lastmodified").value(LAST_MODIFIED_STRING))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 부분 수정")
    void edit2() throws Exception {
        //given
        ReservationEditRequest request = ReservationEditRequest.builder()
                .startTime(START_TIME.strValue)
                .endTime(END_TIME.strValue)
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
                .date(LocalDate.now().minusDays(1))
                .startTime(START_TIME.strValue)
                .endTime(START_TIME.strValue)
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
                .date(DATE)
                .startTime(START_TIME.strValue)
                .endTime(END_TIME.strValue)
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