package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.member.request.MemberEditRequest;
import Dompoo.Hongpoong.api.dto.member.request.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.response.MemberStatusResponse;
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

    private static final Long ID  = 1L;
    private static final Long ID2  = 2L;
    private static final String EMAIL = "dompoo@gmail.com";
    private static final String EMAIL2 = "yoonH@gmail.com";
    private static final String NAME = "이창근";
    private static final String NAME2 = "강윤호";
    private static final String NICKNAME = "불꽃남자";
    private static final String NICKNAME2 = "물남자";
    private static final String CLUB = "산틀";
    private static final String CLUB2 = "악반";
    private static final String NEW_USERNAME = "dompoo2";
    private static final String NEW_PASSWORD = "qwer";
    private static final Integer ENROLLMENT_NUMBER = 19;
    private static final Integer ENROLLMENT_NUMBER2 = 18;
    private static final String PROFILE_IMAGE_URL = "image.com/1";
    private static final String PROFILE_IMAGE_URL2 = "image.com/2";
    private static final Boolean PUSH_ALARM = true;
    private static final Role ROLE = Role.MEMBER;
    private static final Role ROLE2 = Role.LEADER;
    
    //회원 전체 조회
    @Test
    @DisplayName("회원 전체 조회")
    void test() throws Exception {
        //given
        when(memberService.findAllMember()).thenReturn(List.of(
                MemberResponse.builder()
                        .memberId(ID)
                        .email(EMAIL)
                        .name(NAME)
                        .nickname(NICKNAME)
                        .club(CLUB)
                        .enrollmentNumber(ENROLLMENT_NUMBER)
                        .profileImageUrl(PROFILE_IMAGE_URL)
                        .role(ROLE.korName)
                        .build(),
                MemberResponse.builder()
                        .memberId(ID2)
                        .email(EMAIL2)
                        .name(NAME2)
                        .nickname(NICKNAME2)
                        .club(CLUB2)
                        .enrollmentNumber(ENROLLMENT_NUMBER2)
                        .profileImageUrl(PROFILE_IMAGE_URL2)
                        .role(ROLE2.korName)
                        .build()
        ));
        
        //expected
        mockMvc.perform(get("/member"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].memberId").value(ID))
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andExpect(jsonPath("$[0].nickname").value(NICKNAME))
                .andExpect(jsonPath("$[0].club").value(CLUB))
                .andExpect(jsonPath("$[0].enrollmentNumber").value(ENROLLMENT_NUMBER))
                .andExpect(jsonPath("$[0].profileImageUrl").value(PROFILE_IMAGE_URL))
                .andExpect(jsonPath("$[0].role").value(ROLE.korName))
                .andExpect(jsonPath("$[1].memberId").value(ID2))
                .andExpect(jsonPath("$[1].email").value(EMAIL2))
                .andExpect(jsonPath("$[1].name").value(NAME2))
                .andExpect(jsonPath("$[1].nickname").value(NICKNAME2))
                .andExpect(jsonPath("$[1].club").value(CLUB2))
                .andExpect(jsonPath("$[1].enrollmentNumber").value(ENROLLMENT_NUMBER2))
                .andExpect(jsonPath("$[1].profileImageUrl").value(PROFILE_IMAGE_URL2))
                .andExpect(jsonPath("$[1].role").value(ROLE2.korName));
    }

    //로그인 정보
    @Test
    @DisplayName("로그인 정보")
    void findMyMemberDetail() throws Exception {
        //given
        when(memberService.findMyMemberDetail(any())).thenReturn(MemberStatusResponse.builder()
                .memberId(ID)
                .email(EMAIL)
                .name(NAME)
                .nickname(NICKNAME)
                .club(CLUB)
                .enrollmentNumber(ENROLLMENT_NUMBER)
                .profileImageUrl(PROFILE_IMAGE_URL)
                .push(PUSH_ALARM)
                .build());
        
        //expected
        mockMvc.perform(get("/member/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(ID))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.nickname").value(NICKNAME))
                .andExpect(jsonPath("$.club").value(CLUB))
                .andExpect(jsonPath("$.enrollmentNumber").value(ENROLLMENT_NUMBER))
                .andExpect(jsonPath("$.profileImageUrl").value(PROFILE_IMAGE_URL))
                .andExpect(jsonPath("$.push").value(PUSH_ALARM))
                .andDo(print());
    }

    //회원정보 수정
    @Test
    @DisplayName("회원정보 수정")
    void edit() throws Exception {
        //given
        MemberEditRequest request = MemberEditRequest.builder()
                .name(NEW_USERNAME)
                .password(NEW_PASSWORD)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/member")
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
    
    //권한 변경
    @Test
    @DisplayName("권한 변경")
    void editMemberRole() throws Exception {
        //given
        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .role(ROLE)
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(patch("/member/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
        
    }
    
    //회원삭제
    @Test
    @DisplayName("회원삭제")
    void deleteAdmin() throws Exception {
        //given
        
        //expected
        mockMvc.perform(delete("/member/{id}", ID))
                .andExpect(status().isOk())
                .andDo(print());
    }
    
    //권한 변경 - 관리자
    @Test
    @DisplayName("어드민 권한 변경")
    void editMemberRoleByAdmin() throws Exception {
        //given
        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .role(ROLE)
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(patch("/member/manage/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
        
    }

    //회원삭제 - 관리자
    @Test
    @DisplayName("어드민 회원삭제")
    void deleteAdminByAdmin() throws Exception {
        //given

        //expected
        mockMvc.perform(delete("/member/manage/{id}", ID))
                .andExpect(status().isOk())
                .andDo(print());
    }
}