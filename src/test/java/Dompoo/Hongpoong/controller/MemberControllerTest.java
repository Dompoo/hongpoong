package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.request.member.MemberEditRequest;
import Dompoo.Hongpoong.api.dto.request.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.response.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.response.member.MemberStatusResponse;
import Dompoo.Hongpoong.config.MyWebMvcTest;
import Dompoo.Hongpoong.domain.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends MyWebMvcTest {

    private final Long ID  = 1L;
    private final Long ID2  = 2L;
    private final String EMAIL = "dompoo@gmail.com";
    private final String EMAIL2 = "yoonH@gmail.com";
    private final String USERNAME = "dompoo";
    private final String USERNAME2 = "yoonH";
    private final String PASSWORD = "1234";
    private final String CLUB = "산틀";
    private final String CLUB2 = "악반";
    private final String NEW_USERNAME = "dompoo2";
    private final String NEW_PASSWORD = "qwer";

    //로그인 정보
    @Test
    @DisplayName("로그인 정보")
    void getMyDetail() throws Exception {
        //given
        when(memberService.getMyDetail(any())).thenReturn(MemberStatusResponse.builder()
                .id(ID)
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .club(CLUB)
                .build());
        
        //expected
        mockMvc.perform(get("/member/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.password").value(PASSWORD))
                .andExpect(jsonPath("$.club").value(CLUB))
                .andDo(print());
    }

    //회원정보 수정
    @Test
    @DisplayName("회원정보 수정")
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
    void getList() throws Exception {
        //given
        when(memberService.getAllMember()).thenReturn(List.of(
                MemberResponse.builder()
                        .id(ID)
                        .email(EMAIL)
                        .username(USERNAME)
                        .club(CLUB)
                        .build(),
                MemberResponse.builder()
                        .id(ID2)
                        .email(EMAIL2)
                        .username(USERNAME2)
                        .club(CLUB2)
                        .build()
        ));

        //expected
        mockMvc.perform(get("/member/manage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[0].username").value(USERNAME))
                .andExpect(jsonPath("$[0].club").value(CLUB))
                .andExpect(jsonPath("$[1].id").value(ID2))
                .andExpect(jsonPath("$[1].email").value(EMAIL2))
                .andExpect(jsonPath("$[1].username").value(USERNAME2))
                .andExpect(jsonPath("$[1].club").value(CLUB2))
                .andDo(print());
    }

    //회원삭제 - 관리자
    @Test
    @DisplayName("회원삭제")
    void deleteAdmin() throws Exception {
        //given

        //expected
        mockMvc.perform(delete("/member/manage/{id}", ID))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //권한 변경 - 관리자
    @Test
    @DisplayName("권한 변경")
    void editMemberRole() throws Exception {
        //given
        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .role(Role.ROLE_LEADER)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/member/manage/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

    }

}