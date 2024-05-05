package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.WithMockMember;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.request.member.MemberEditRequest;
import Dompoo.Hongpoong.request.member.MemberRoleEditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
class MemberControllerTest {

    @Autowired
    private MemberRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final String EMAIL = "dompoo@gmail.com";
    private final String USERNAME = "dompoo";
    private final String PASSWORD = "1234";
    private final String NEW_USERNAME = "dompoo2";
    private final String NEW_PASSWORD = "qwer";

    @AfterEach
    void setUp() {
        repository.deleteAll();
    }

    //로그인 정보
    @Test
    @DisplayName("로그인 정보")
    @WithMockMember
    void getStatus() throws Exception {
        //expected
        mockMvc.perform(get("/member/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("dompoo@gmail.com"))
                .andExpect(jsonPath("$.username").value("창근"))
                .andExpect(jsonPath("$.password").value("1234"))
                .andDo(print());
    }

    //회원정보 수정
    @Test
    @DisplayName("회원정보 수정")
    @WithMockMember
    void edit() throws Exception {
        //given
        MemberEditRequest request = MemberEditRequest.builder()
                .username(NEW_USERNAME)
                .password1(NEW_PASSWORD)
                .password2(NEW_PASSWORD)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //회원탈퇴
    @Test
    @DisplayName("회원탈퇴")
    @WithMockMember
    void deleteOne() throws Exception {
        //given

        //expected
        mockMvc.perform(delete("/member"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //회원 리스트 - 관리자
    @Test
    @DisplayName("회원 리스트 조회")
    @WithMockMember(role = "ROLE_ADMIN")
    void getList() throws Exception {
        //given
        repository.save(Member.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build());

        repository.save(Member.builder()
                .email(EMAIL)
                .username(NEW_USERNAME)
                .password(NEW_PASSWORD)
                .build());

        //expected
        mockMvc.perform(get("/member/manage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andDo(print());
    }

    @Test
    @DisplayName("회원은 회원 리스트 조회할 수 없다.")
    @WithMockMember
    void getListFail() throws Exception {
        //given
        repository.save(Member.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build());

        repository.save(Member.builder()
                .email(EMAIL)
                .username(NEW_USERNAME)
                .password(NEW_PASSWORD)
                .build());

        //expected
        mockMvc.perform(get("/member/manage"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("[인증오류] 권한이 없습니다."))
                .andDo(print());
    }

    //회원삭제 - 관리자
    @Test
    @DisplayName("회원삭제")
    @WithMockMember(role = "ROLE_ADMIN")
    void deleteAdmin() throws Exception {
        //given
        Member member = repository.save(Member.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build());

        //expected
        mockMvc.perform(delete("/member/manage/{id}", member.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원은 회원삭제할 수 없다.")
    @WithMockMember
    void deleteAdminFail() throws Exception {
        //given
        Member member = repository.save(Member.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build());

        //expected
        mockMvc.perform(delete("/member/manage/{id}", member.getId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("[인증오류] 권한이 없습니다."))
                .andDo(print());
    }

    //권한 변경 - 관리자
    @Test
    @DisplayName("권한 변경")
    @WithMockMember(role = "ROLE_ADMIN")
    void changeAuth() throws Exception {
        //given
        Member member = repository.save(Member.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build());

        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .isAdmin(true)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/member/manage/{id}", member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("회원은 권한 변경할 수 없다.")
    @WithMockMember
    void changeAuthFail() throws Exception {
        //given
        Member member = repository.save(Member.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build());

        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .isAdmin(true)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/member/manage/{id}", member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("[인증오류] 권한이 없습니다."))
                .andDo(print());
    }

}