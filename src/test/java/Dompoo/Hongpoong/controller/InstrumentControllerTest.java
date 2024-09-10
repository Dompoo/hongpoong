package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentEditRequest;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentBorrowResponse;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentResponse;
import Dompoo.Hongpoong.config.MyWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InstrumentControllerTest extends MyWebMvcTest {
    
    private static final Long RESERVATION_ID = 1L;
    private static final Long INSTRUMENT_ID = 1L;
    private static final Long INSTRUMENT2_ID = 2L;
    private static final String INSTRUMENT_CLUB = "산틀";
    private static final String INSTRUMENT2_CLUB = "악반";
    private static final String INSTRUMENT_TYPE = "소고";
    private static final String INSTRUMENT2_TYPE = "장구";
    private static final LocalDate RETURN_DATE = LocalDate.now().plusDays(10);
    private static final String RETURN_DATE_STRING = RETURN_DATE.toString();
    private static final LocalTime RETURN_TIME = LocalTime.of(15, 0);
    private static final String RETURN_TIME_STRING = RETURN_TIME.toString() + ":00";

    @Test
    @DisplayName("악기 추가")
    void createInstrument() throws Exception {
        //given
        InstrumentCreateRequest request = InstrumentCreateRequest.builder()
                .type(INSTRUMENT_TYPE)
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
    void createInstrumentFail1() throws Exception {
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
    @DisplayName("악기 추가시 악기는 공백일 수 없다.")
    void createInstrumentFail2() throws Exception {
        //given
        InstrumentCreateRequest request = InstrumentCreateRequest.builder()
                .type(" ")
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
    @DisplayName("다른 동아리의 악기 조회")
    void listOther() throws Exception {
        //given
        when(instrumentService.findAllOtherClubInstrument(any())).thenReturn(List.of(
                InstrumentResponse.builder()
                        .instrumentId(INSTRUMENT_ID)
                        .club(INSTRUMENT_CLUB)
                        .type(INSTRUMENT_TYPE)
                        .build(),
                InstrumentResponse.builder()
                        .instrumentId(INSTRUMENT2_ID)
                        .club(INSTRUMENT2_CLUB)
                        .type(INSTRUMENT2_TYPE)
                        .build()
        ));

        //expected
        mockMvc.perform(get("/instrument"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].instrumentId").value(INSTRUMENT_ID))
                .andExpect(jsonPath("$[0].club").value(INSTRUMENT_CLUB))
                .andExpect(jsonPath("$[0].type").value(INSTRUMENT_TYPE))
                .andExpect(jsonPath("$[1].instrumentId").value(INSTRUMENT2_ID))
                .andExpect(jsonPath("$[1].club").value(INSTRUMENT2_CLUB))
                .andExpect(jsonPath("$[1].type").value(INSTRUMENT2_TYPE))
                .andDo(print());
    }

    @Test
    @DisplayName("내 동아리의 악기 조회")
    void listMe() throws Exception {
        //given
        when(instrumentService.findAllMyClubInstrument(any())).thenReturn(List.of(
                InstrumentResponse.builder()
                        .instrumentId(INSTRUMENT_ID)
                        .club(INSTRUMENT_CLUB)
                        .type(INSTRUMENT_TYPE)
                        .build(),
                InstrumentResponse.builder()
                        .instrumentId(INSTRUMENT2_ID)
                        .club(INSTRUMENT2_CLUB)
                        .type(INSTRUMENT2_TYPE)
                        .build()
        ));
        
        //expected
        mockMvc.perform(get("/instrument/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].instrumentId").value(INSTRUMENT_ID))
                .andExpect(jsonPath("$[0].club").value(INSTRUMENT_CLUB))
                .andExpect(jsonPath("$[0].type").value(INSTRUMENT_TYPE))
                .andExpect(jsonPath("$[1].instrumentId").value(INSTRUMENT2_ID))
                .andExpect(jsonPath("$[1].club").value(INSTRUMENT2_CLUB))
                .andExpect(jsonPath("$[1].type").value(INSTRUMENT2_TYPE))
                .andDo(print());
    }

    @Test
    @DisplayName("악기 빌리기")
    void borrow() throws Exception {
        //given
        when(instrumentService.borrowInstrument(any(), any())).thenReturn(InstrumentBorrowResponse.builder()
                .instrumentId(INSTRUMENT_ID)
                .returnDate(RETURN_DATE)
                .returnTime(RETURN_TIME)
                .build());

        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .reservationId(RESERVATION_ID)
                .instrumentId(INSTRUMENT_ID)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/instrument/borrow")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instrumentId").value(INSTRUMENT_ID))
                .andExpect(jsonPath("$.returnDate").value(RETURN_DATE_STRING))
                .andExpect(jsonPath("$.returnTime").value(RETURN_TIME_STRING))
                .andDo(print());
    }
    
    @Test
    @DisplayName("악기를 빌릴 때 예약 id는 비어있을 수 없다.")
    void borrowFail() throws Exception {
        //given
        when(instrumentService.borrowInstrument(any(), any())).thenReturn(InstrumentBorrowResponse.builder()
                .instrumentId(INSTRUMENT_ID)
                .returnDate(RETURN_DATE)
                .returnTime(RETURN_TIME)
                .build());
        
        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .instrumentId(INSTRUMENT_ID)
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(post("/instrument/borrow")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("[악기 대여할 예약은 비어있을 수 없습니다.]"))
                .andDo(print());
    }
    
    @Test
    @DisplayName("악기를 빌릴 때 악기 id는 비어있을 수 없다.")
    void borrowFail2() throws Exception {
        //given
        when(instrumentService.borrowInstrument(any(), any())).thenReturn(InstrumentBorrowResponse.builder()
                .instrumentId(INSTRUMENT_ID)
                .returnDate(RETURN_DATE)
                .returnTime(RETURN_TIME)
                .build());
        
        InstrumentBorrowRequest request = InstrumentBorrowRequest.builder()
                .reservationId(RESERVATION_ID)
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(post("/instrument/borrow")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("[악기는 비어있을 수 없습니다.]"))
                .andDo(print());
    }
    
    @Test
    @DisplayName("악기 반납하기")
    void returnInstrument() throws Exception {
        //given

        //expected
        mockMvc.perform(post("/instrument/return/{id}", INSTRUMENT_ID))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("악기 상세 조회")
    void findInstrumentDetail() throws Exception {
        //given
        when(instrumentService.findInstrumentDetail(any())).thenReturn(InstrumentResponse.builder()
                .instrumentId(INSTRUMENT_ID)
                .type(INSTRUMENT_TYPE)
                .club(INSTRUMENT_CLUB)
                .build());
        
        //expected
        mockMvc.perform(get("/instrument/{id}", INSTRUMENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instrumentId").value(INSTRUMENT_ID))
                .andExpect(jsonPath("$.type").value(INSTRUMENT_TYPE))
                .andExpect(jsonPath("$.club").value(INSTRUMENT_CLUB))
                .andDo(print());
    }
    
    @Test
    @DisplayName("악기 수정")
    void editInstrumentOne() throws Exception {
        //given
        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .available(true)
                .type(INSTRUMENT_TYPE)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/instrument/{id}", INSTRUMENT_ID)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("악기 삭제")
    void deleteInstrumentOne() throws Exception {
        //given
        
        //expected
        mockMvc.perform(delete("/instrument/{id}", INSTRUMENT_ID))
                .andExpect(status().isOk())
                .andDo(print());
    }
    
    @Test
    @DisplayName("어드민 악기 수정")
    void editInstrumentByAdmin() throws Exception {
        //given
        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .available(true)
                .type(INSTRUMENT_TYPE)
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(patch("/instrument/manage/{id}", INSTRUMENT_ID)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }
    
    @Test
    @DisplayName("어드민 악기 삭제")
    void deleteInstrumentByAdmin() throws Exception {
        //given
        
        //expected
        mockMvc.perform(delete("/instrument/manage/{id}", INSTRUMENT_ID))
                .andExpect(status().isOk())
                .andDo(print());
    }
}