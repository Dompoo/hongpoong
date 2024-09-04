package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentEditRequest;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentBorrowResponse;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentResponse;
import Dompoo.Hongpoong.config.MyWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
    private static final LocalDate RETURN_DATE = LocalDate.of(2000, 5, 17);
    private static final String RETURN_DATE_STRING = "2000-05-17";
    private static final int RETURN_TIME = 10;

    @Test
    @DisplayName("악기 추가")
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
    @DisplayName("다른 동아리의 악기 조회")
    void listOther() throws Exception {
        //given
        when(instrumentService.getListOther(any())).thenReturn(List.of(
                InstrumentResponse.builder()
                        .id(INSTRUMENT_ID)
                        .club(INSTRUMENT_CLUB)
                        .type(INSTRUMENT_TYPE)
                        .build(),
                InstrumentResponse.builder()
                        .id(INSTRUMENT2_ID)
                        .club(INSTRUMENT2_CLUB)
                        .type(INSTRUMENT2_TYPE)
                        .build()
        ));

        //expected
        mockMvc.perform(get("/instrument"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(INSTRUMENT_ID))
                .andExpect(jsonPath("$[0].club").value(INSTRUMENT_CLUB))
                .andExpect(jsonPath("$[0].type").value(INSTRUMENT_TYPE))
                .andExpect(jsonPath("$[1].id").value(INSTRUMENT2_ID))
                .andExpect(jsonPath("$[1].club").value(INSTRUMENT2_CLUB))
                .andExpect(jsonPath("$[1].type").value(INSTRUMENT2_TYPE))
                .andDo(print());
    }

    @Test
    @DisplayName("내 동아리의 악기 조회")
    void listMe() throws Exception {
        //given
        when(instrumentService.getList(any())).thenReturn(List.of(
                InstrumentResponse.builder()
                        .id(INSTRUMENT_ID)
                        .club(INSTRUMENT_CLUB)
                        .type(INSTRUMENT_TYPE)
                        .build(),
                InstrumentResponse.builder()
                        .id(INSTRUMENT2_ID)
                        .club(INSTRUMENT2_CLUB)
                        .type(INSTRUMENT2_TYPE)
                        .build()
        ));
        
        //expected
        mockMvc.perform(get("/instrument/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(INSTRUMENT_ID))
                .andExpect(jsonPath("$[0].club").value(INSTRUMENT_CLUB))
                .andExpect(jsonPath("$[0].type").value(INSTRUMENT_TYPE))
                .andExpect(jsonPath("$[1].id").value(INSTRUMENT2_ID))
                .andExpect(jsonPath("$[1].club").value(INSTRUMENT2_CLUB))
                .andExpect(jsonPath("$[1].type").value(INSTRUMENT2_TYPE))
                .andDo(print());
    }

    @Test
    @DisplayName("악기 빌리기")
    void borrow() throws Exception {
        //given
        when(instrumentService.borrowOne(any(), any())).thenReturn(InstrumentBorrowResponse.builder()
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
                .andExpect(jsonPath("$.returnTime").value(RETURN_TIME))
                .andDo(print());
    }
    
    @Test
    @DisplayName("악기 반납하기")
    void returnOne() throws Exception {
        //given

        //expected
        mockMvc.perform(post("/instrument/return/{id}", INSTRUMENT_ID))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("악기 1개 조회")
    void getOne() throws Exception {
        //given
        when(instrumentService.getOne(any())).thenReturn(InstrumentResponse.builder()
                .id(INSTRUMENT_ID)
                .type(INSTRUMENT_TYPE)
                .club(INSTRUMENT_CLUB)
                .build());
        
        //expected
        mockMvc.perform(get("/instrument/{id}", INSTRUMENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(INSTRUMENT_ID))
                .andExpect(jsonPath("$.type").value(INSTRUMENT_TYPE))
                .andExpect(jsonPath("$.club").value(INSTRUMENT_CLUB))
                .andDo(print());
    }
    
    @Test
    @DisplayName("악기 수정")
    void editOne() throws Exception {
        //given
        InstrumentEditRequest request = InstrumentEditRequest.builder()
                .available(true)
                .type(1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/instrument/{id}", INSTRUMENT_ID)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("악기 삭제")
    void deleteOne() throws Exception {
        //given
        
        //expected
        mockMvc.perform(delete("/instrument/{id}", INSTRUMENT_ID))
                .andExpect(status().isOk())
                .andDo(print());
    }
}