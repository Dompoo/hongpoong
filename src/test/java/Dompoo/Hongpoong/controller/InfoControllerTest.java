package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.WithMockMember;
import Dompoo.Hongpoong.domain.Info;
import Dompoo.Hongpoong.repository.InfoRepository;
import Dompoo.Hongpoong.request.info.InfoCreateRequest;
import Dompoo.Hongpoong.request.info.InfoEditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class InfoControllerTest {

    @Autowired
    private InfoRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String TITLE = "공지사항 제목";
    private static final String CONTENT = "공지사항 내용";
    private static final String NEW_TITLE = "새로운 공지사항 제목";
    private static final String NEW_CONTENT = "새로운 공지사항 내용";

    //공지사항 추가 테스트
    @Test
    @DisplayName("공지사항 추가")
    @WithMockMember
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

    //공지사항 전체 조회 테스트 추가
    @Test
    @DisplayName("공지사항 전체 조회")
    @WithMockMember
    void getAll() throws Exception {
        repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        repository.save(Info.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build());

        //expected
        mockMvc.perform(get("/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$.[0].title").value(TITLE))
                .andExpect(jsonPath("$.[1].title").value(NEW_TITLE))
                .andDo(print());
    }

    //공지사항 상세 조회 테스트 추가
    @Test
    @DisplayName("공지사항 상세 조회")
    @WithMockMember
    void getOne() throws Exception {
        //given
        Info save = repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        //expected
        mockMvc.perform(get("/info/{id}", save.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.content").value(CONTENT))
                .andDo(print());
    }

    //공지사항 상세 조회 실패 테스트 추가
    @Test
    @DisplayName("존재하지 않는 공지사항 상세 조회")
    @WithMockMember
    void getOneFail() throws Exception {
        //given
        Info save = repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        //expected
        mockMvc.perform(get("/info/{id}", save.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 공지사항입니다."))
                .andDo(print());
    }

    //공지사항 수정 테스트 추가
    @Test
    @DisplayName("공지사항 전체 수정")
    @WithMockMember
    void editOne() throws Exception {
        //given
        Info save = repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        InfoEditRequest request = InfoEditRequest.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/info/{id}", save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("공지사항 일부 수정")
    @WithMockMember
    void editOne1() throws Exception {
        //given
        Info save = repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        InfoEditRequest request = InfoEditRequest.builder()
                .content(NEW_CONTENT)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/info/{id}", save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //공지사항 수정 실패 테스트 추가
    @Test
    @DisplayName("존재하지 않는 공지사항 수정")
    @WithMockMember
    void editOneFail() throws Exception {
        //given
        Info save = repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        InfoEditRequest request = InfoEditRequest.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/info/{id}", save.getId() + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 공지사항입니다."))
                .andDo(print());
    }

    //공지사항 삭제 테스트 추가
    @Test
    @DisplayName("공지사항 삭제")
    @WithMockMember
    void deleteOne() throws Exception {
        //given
        Info save = repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        //expected
        mockMvc.perform(delete("/info/{id}", save.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //공지사항 삭제 실패 테스트 추가
    @Test
    @DisplayName("공지사항 삭제 실패")
    @WithMockMember
    void deleteOneFail() throws Exception {
        //given
        Info save = repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        //expected
        mockMvc.perform(delete("/info/{id}", save.getId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 공지사항입니다."))
                .andDo(print());
    }
}