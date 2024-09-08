package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.info.request.InfoCreateRequest;
import Dompoo.Hongpoong.api.dto.info.request.InfoEditRequest;
import Dompoo.Hongpoong.api.dto.info.response.InfoDetailResponse;
import Dompoo.Hongpoong.api.dto.info.response.InfoResponse;
import Dompoo.Hongpoong.config.MyWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InfoControllerTest extends MyWebMvcTest {
    
    private static final Long ID = 1L;
    private static final Long ID2 = 2L;
    private static final String TITLE = "공지사항 제목";
    private static final String TITLE2 = "공지사항 제목2";
    private static final LocalDateTime DATE = LocalDateTime.of(2000, 5, 17, 10, 30, 0);
    private static final String DATE_STRING = "2000-05-17T10:30:00";
    private static final LocalDateTime DATE2 = LocalDateTime.of(2000, 10, 20, 10, 30, 0);
    private static final String DATE2_STRING = "2000-10-20T10:30:00";
    private static final String CONTENT = "공지사항 내용";
    private static final String NEW_TITLE = "새로운 공지사항 제목";
    private static final String NEW_CONTENT = "새로운 공지사항 내용";
    
    @Test
    @DisplayName("공지사항 추가")
    void addOne() throws Exception {
        //given
        InfoCreateRequest request = InfoCreateRequest.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("공지사항 전체 조회")
    void getAll() throws Exception {
        //given
        when(infoService.findAllInfo()).thenReturn(List.of(
                InfoResponse.builder()
                        .infoId(ID)
                        .title(TITLE)
                        .date(DATE)
                        .build(),
                InfoResponse.builder()
                        .infoId(ID2)
                        .title(TITLE2)
                        .date(DATE2)
                        .build()
        ));
        
        //expected
        mockMvc.perform(get("/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$.[0].id").value(ID))
                .andExpect(jsonPath("$.[0].title").value(TITLE))
                .andExpect(jsonPath("$.[0].date").value(DATE_STRING))
                .andExpect(jsonPath("$.[1].id").value(ID2))
                .andExpect(jsonPath("$.[1].title").value(TITLE2))
                .andExpect(jsonPath("$.[1].date").value(DATE2_STRING))
                .andDo(print());
    }

    @Test
    @DisplayName("공지사항 상세 조회")
    void getOne() throws Exception {
        //given
        when(infoService.findInfoDetail(any())).thenReturn(InfoDetailResponse.builder()
                .infoId(ID)
                .title(TITLE)
                .content(CONTENT)
                .date(DATE)
                .build());

        //expected
        mockMvc.perform(get("/info/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.content").value(CONTENT))
                .andExpect(jsonPath("$.date").value(DATE_STRING))
                .andDo(print());
    }

    @Test
    @DisplayName("공지사항 전체 수정")
    void editOne() throws Exception {
        //given
        InfoEditRequest request = InfoEditRequest.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/info/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("공지사항 일부 수정")
    void editOne1() throws Exception {
        //given
        InfoEditRequest request = InfoEditRequest.builder()
                .content(NEW_CONTENT)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/info/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //공지사항 삭제 테스트 추가
    @Test
    @DisplayName("공지사항 삭제")
    void deleteOne() throws Exception {
        //given

        //expected
        mockMvc.perform(delete("/info/{id}", ID))
                .andExpect(status().isOk())
                .andDo(print());
    }
}