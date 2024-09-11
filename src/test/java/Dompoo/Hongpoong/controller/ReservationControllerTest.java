package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.config.MyWebMvcTest;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private static final LocalDate DATE = LocalDate.of(2040, 5, 17);
    private static final String DATE_YEAR = "2040";
    private static final String DATE_MONTH = "5";
    private static final String DATE_STRING = "2040-05-17";
    private static final ReservationTime START_TIME = ReservationTime.TIME_0900;
    private static final LocalTime START_TIME_LOCALTIME = START_TIME.localTime;
    private static final ReservationTime END_TIME = ReservationTime.TIME_1130;
    private static final LocalTime END_TIME_LOCALTIME = END_TIME.localTime;
    private static final String MESSAGE = "안녕하세용!";
    
    private static final Long RESERVATION_ID2 = 2L;
    private static final String NAME2 = "윤호";
    private static final String EMAIL2 = "yoonH@gmail.com";
    private static final LocalDate DATE2 = LocalDate.of(2040, 10, 10);
    private static final String DATE2_STRING = "2040-10-10";
    private static final ReservationTime START_TIME2 = ReservationTime.TIME_1000;
    private static final LocalTime START_TIME2_LOCALTIME = START_TIME2.localTime;
    private static final ReservationTime END_TIME2 = ReservationTime.TIME_1200;
    private static final LocalTime END_TIME2_LOCALTIME = END_TIME2.localTime;
    private static final String MESSAGE2 = "하이!";
    
    private static final LocalDateTime LAST_MODIFIED = LocalDateTime.of(2030, 10, 10, 10, 30);
    private static final String LAST_MODIFIED_STRING = "2030-10-10T10:30:00";
    
    private static final List<Long> PARTICIPATER_IDS = List.of(1L, 2L, 3L);

    @Test
    @DisplayName("예약 추가")
    void add() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .participaterIds(PARTICIPATER_IDS)
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
    @DisplayName("예약 추가시 시작 시간은 비어있을 수 없다.")
    void addFail1() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(DATE)
                .endTime(END_TIME)
                .participaterIds(PARTICIPATER_IDS)
                .message("")
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[시작 시간은 비어있을 수 없습니다.]"))
                .andDo(print());
    }
    
    @Test
    @DisplayName("예약 추가시 종료 시간은 비어있을 수 없다.")
    void addFail2() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(DATE)
                .startTime(START_TIME)
                .participaterIds(PARTICIPATER_IDS)
                .message("")
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[종료 시간은 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("예약 추가시 날짜는 과거일 수 없다.")
    void addFail3() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(LocalDate.now().minusDays(1))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .participaterIds(PARTICIPATER_IDS)
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
    @DisplayName("예약 추가시 날짜는 비어있을 수 없다.")
    void addFail4() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .startTime(START_TIME)
                .endTime(END_TIME)
                .participaterIds(PARTICIPATER_IDS)
                .message("")
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[예약 날짜는 비어있을 수 없습니다.]"))
                .andDo(print());
    }
    
    @Test
    @DisplayName("예약 추가시 참가자 id는 비어있을 수 없다.")
    void addFail5() throws Exception {
        //given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(post("/reservation")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[참가자는 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("연도와 달로 예약 전체 조회")
    void findAllReservationByYearAndMonth() throws Exception {
        //given
        when(reservationService.findAllReservationOfYearAndMonth(any(), any())).thenReturn(List.of(getReservationResponse()));

        //expected
        mockMvc.perform(get("/reservation/year-month")
                        .queryParam("year", DATE_YEAR)
                        .queryParam("month", DATE_MONTH)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].reservationId").value(RESERVATION_ID))
                .andExpect(jsonPath("$[0].creatorName").value(NAME))
                .andExpect(jsonPath("$[0].date").value(DATE_STRING))
                .andExpect(jsonPath("$[0].startTime").value(START_TIME_LOCALTIME + ":00"))
                .andExpect(jsonPath("$[0].endTime").value(END_TIME_LOCALTIME + ":00"))
                .andExpect(jsonPath("$[0].message").value(MESSAGE))
                .andExpect(jsonPath("$[0].lastmodified").value(LAST_MODIFIED_STRING))
                .andDo(print());
    }
    
    @Test
    @DisplayName("날짜로 예약 전체 조회")
    void findAllReservationByDay() throws Exception {
        //given
        when(reservationService.findAllReservationOfDate(any())).thenReturn(List.of(getReservationResponse()));
        
        //expected
        mockMvc.perform(get("/reservation/day")
                        .queryParam("date", String.valueOf(DATE))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].reservationId").value(RESERVATION_ID))
                .andExpect(jsonPath("$[0].creatorName").value(NAME))
                .andExpect(jsonPath("$[0].date").value(DATE_STRING))
                .andExpect(jsonPath("$[0].startTime").value(START_TIME_LOCALTIME + ":00"))
                .andExpect(jsonPath("$[0].endTime").value(END_TIME_LOCALTIME + ":00"))
                .andExpect(jsonPath("$[0].message").value(MESSAGE))
                .andExpect(jsonPath("$[0].lastmodified").value(LAST_MODIFIED_STRING))
                .andDo(print());
    }
    
    @Test
    @DisplayName("해야하는 오늘의 예약 전체 조회")
    void findAllReservationTodo() throws Exception {
        //given
        when(reservationService.findAllTodoReservationOfToday(any(), any())).thenReturn(List.of(getReservationResponse()));
        
        //expected
        mockMvc.perform(get("/reservation/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].reservationId").value(RESERVATION_ID))
                .andExpect(jsonPath("$[0].creatorName").value(NAME))
                .andExpect(jsonPath("$[0].date").value(DATE_STRING))
                .andExpect(jsonPath("$[0].startTime").value(START_TIME_LOCALTIME + ":00"))
                .andExpect(jsonPath("$[0].endTime").value(END_TIME_LOCALTIME + ":00"))
                .andExpect(jsonPath("$[0].message").value(MESSAGE))
                .andExpect(jsonPath("$[0].lastmodified").value(LAST_MODIFIED_STRING))
                .andDo(print());
    }
    
    @Test
    @DisplayName("예약 상세 조회")
    void findReservationDetail() throws Exception {
        //given
        when(reservationService.findReservationDetail(any())).thenReturn(getReservationDetailResponse());
        
        //expected
        mockMvc.perform(get("/reservation/{id}", RESERVATION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(RESERVATION_ID))
                .andExpect(jsonPath("$.creatorName").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.date").value(DATE_STRING))
                .andExpect(jsonPath("$.startTime").value(START_TIME_LOCALTIME + ":00"))
                .andExpect(jsonPath("$.endTime").value(END_TIME_LOCALTIME + ":00"))
                .andExpect(jsonPath("$.message").value(MESSAGE))
                .andExpect(jsonPath("$.lastmodified").value(LAST_MODIFIED_STRING))
                .andExpect(jsonPath("$.participators.size()").value(2))
                .andDo(print());
    }
    
    @Test
    @DisplayName("예약 시간 연장")
    void extendReservationTime() throws Exception {
        //given
        
        //expected
        mockMvc.perform(patch("/reservation/{id}/extend", RESERVATION_ID))
                .andExpect(status().isOk())
                .andDo(print());
    }
    
    @Test
    @DisplayName("예약 수정")
    void editReservation() throws Exception {
        //given
        when(reservationService.editReservation(any(), any(), any(), any())).thenReturn(getReservationEditedDetailResponse());
        
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
                .andExpect(jsonPath("$.reservationId").value(RESERVATION_ID))
                .andExpect(jsonPath("$.creatorName").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.date").value(DATE2_STRING))
                .andExpect(jsonPath("$.startTime").value(START_TIME2_LOCALTIME + ":00"))
                .andExpect(jsonPath("$.endTime").value(END_TIME2_LOCALTIME + ":00"))
                .andExpect(jsonPath("$.message").value(MESSAGE))
                .andExpect(jsonPath("$.lastmodified").value(LAST_MODIFIED_STRING))
                .andExpect(jsonPath("$.participators.size()").value(2))
                .andDo(print());
    }
    
    @Test
    @DisplayName("예약 수정시 과거날짜일 수 없다.")
    void editReservationByAdminFail2() throws Exception {
        //given
        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.now().minusDays(1))
                .startTime(START_TIME)
                .endTime(END_TIME)
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
    void editReservationByAdminManage() throws Exception {
        //given

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(DATE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/reservation/manage/{id}", RESERVATION_ID)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }
    
    @Test
    @DisplayName("관리자 예약 삭제")
    void deleteByAdmin() throws Exception {
        //given
        
        //expected
        mockMvc.perform(delete("/reservation/manage/{id}", RESERVATION_ID))
                .andExpect(status().isOk())
                .andDo(print());
    }
    
    private static ReservationResponse getReservationResponse() {
        return ReservationResponse.builder()
                .reservationId(RESERVATION_ID)
                .creatorName(NAME)
                .date(DATE)
                .startTime(START_TIME_LOCALTIME)
                .endTime(END_TIME_LOCALTIME)
                .message(MESSAGE)
                .lastmodified(LAST_MODIFIED)
                .build();
    }
    
    private static ReservationDetailResponse getReservationDetailResponse() {
        return ReservationDetailResponse.builder()
                .reservationId(RESERVATION_ID)
                .creatorName(NAME)
                .email(EMAIL)
                .date(DATE)
                .startTime(START_TIME_LOCALTIME)
                .endTime(END_TIME_LOCALTIME)
                .message(MESSAGE)
                .lastmodified(LAST_MODIFIED)
                .participators(List.of(
                        MemberResponse.builder()
                                .name(NAME)
                                .build(),
                        MemberResponse.builder()
                                .name("윤호")
                                .build()
                ))
                .build();
    }
    
    private static ReservationDetailResponse getReservationEditedDetailResponse() {
        return ReservationDetailResponse.builder()
                .reservationId(RESERVATION_ID)
                .creatorName(NAME)
                .email(EMAIL)
                .date(DATE2)
                .startTime(START_TIME2_LOCALTIME)
                .endTime(END_TIME2_LOCALTIME)
                .message(MESSAGE)
                .lastmodified(LAST_MODIFIED)
                .participators(List.of(
                        MemberResponse.builder()
                                .name(NAME)
                                .build(),
                        MemberResponse.builder()
                                .name("윤호")
                                .build()
                ))
                .build();
    }
}