package Dompoo.Hongpoong.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import Dompoo.Hongpoong.api.dto.auth.request.AcceptSignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.request.EmailValidRequest;
import Dompoo.Hongpoong.api.dto.auth.request.LoginRequest;
import Dompoo.Hongpoong.api.dto.auth.request.RejectSignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.request.SignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.response.EmailValidResponse;
import Dompoo.Hongpoong.api.dto.auth.response.LoginResponse;
import Dompoo.Hongpoong.api.dto.auth.response.SignUpResponse;
import Dompoo.Hongpoong.config.MyWebMvcTest;
import Dompoo.Hongpoong.domain.enums.Club;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AuthControllerTest extends MyWebMvcTest {
    
    private static final String NAME = "창근";
    private static final String NICKNAME = "불꽃남자";
    private static final Long SIGNUP_ID = 1L;
    private static final String EMAIL = "dompoo@gmail.com";
    private static final String EMAIL2 = "yoonH@gmail.com";
    private static final Integer ENROLLMENT_NUMBER = 19;
    private static final String PASSWORD = "1234";
    private static final Club CLUB = Club.SANTLE;

    @Test
    @DisplayName("이메일 유효성 검사")
    void checkEmail() throws Exception {
        //given
        when(authService.checkEmailValid(any())).thenReturn(
                EmailValidResponse.builder()
                .valid(true)
                .build());
        
        EmailValidRequest request = EmailValidRequest.builder()
                .email(EMAIL)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청")
    void requestSignup() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .name(NAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .club(CLUB)
                .enrollmentNumber(ENROLLMENT_NUMBER)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청시 유저명은 비어있을 수 없다.")
    void requestSignupFail1() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .club(CLUB)
                .enrollmentNumber(ENROLLMENT_NUMBER)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[이름은 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청시 유저명은 공백일 수 없다.")
    void requestSignupFail2() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .club(CLUB)
                .enrollmentNumber(ENROLLMENT_NUMBER)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[이름은 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청시 비밀번호는 비어있을 수 없다.")
    void requestSignupFail3() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .name(NAME)
                .nickname(NICKNAME)
                .club(CLUB)
                .enrollmentNumber(ENROLLMENT_NUMBER)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[비밀번호는 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청시 이메일은 비어있을 수 없다.")
    void requestSignupFail6() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .name(NAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .club(CLUB)
                .enrollmentNumber(ENROLLMENT_NUMBER)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[이메일은 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청시 이메일은 이메일 형식으로 입력해야 한다.")
    void requestSignupFail7() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email("abc")
                .name(NAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .club(CLUB)
                .enrollmentNumber(ENROLLMENT_NUMBER)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[이메일 형식으로 입력해주세요.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청시 이메일은 공백일 수 없다.")
    void requestSignupFail8() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(" ")
                .name(NAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .club(CLUB)
                .enrollmentNumber(ENROLLMENT_NUMBER)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[이메일 형식으로 입력해주세요.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청시 동아리가 비어있을 수 없다.")
    void requestSignupFail11() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .name(NAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .enrollmentNumber(ENROLLMENT_NUMBER)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[동아리는 비어있을 수 없습니다.]"))
                .andDo(print());
    }
    
    @Test
    @DisplayName("회원가입 요청시 학번이 비어있을 수 없다.")
    void requestSignupFail12() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .name(NAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .club(CLUB)
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[학번은 비어있을 수 없습니다.]"))
                .andDo(print());
    }
    
    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        //given
        when(authService.login(any())).thenReturn(LoginResponse.builder().token("token").build());
        
        LoginRequest request = LoginRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andDo(print());
    }
    
    @Test
    @DisplayName("로그인 요청시 email은 필수값이다.")
    void loginFail1() throws Exception {
        //given
        when(authService.login(any())).thenReturn(LoginResponse.builder().token("token").build());
        
        LoginRequest request = LoginRequest.builder()
                .password(PASSWORD)
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("[이메일을 입력해주세요.]"))
                .andDo(print());
    }
    
    @Test
    @DisplayName("로그인 요청시 password는 필수값이다.")
    void loginFail2() throws Exception {
        //given
        when(authService.login(any())).thenReturn(LoginResponse.builder().token("token").build());
        
        LoginRequest request = LoginRequest.builder()
                .email(EMAIL)
                .build();
        
        String json = objectMapper.writeValueAsString(request);
        
        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("[비밀번호를 입력해주세요.]"))
                .andDo(print());
    }
    
    @Test
    @DisplayName("회원가입 요청 리스트 조회")
    void findAllSignup() throws Exception {
        //given
        when(authService.findAllSignup()).thenReturn(List.of(
                SignUpResponse.builder()
                        .email(EMAIL)
                        .build(),
                SignUpResponse.builder()
                        .email(EMAIL2)
                        .build()
        ));
        
        //expected
        mockMvc.perform(get("/auth/signup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[1].email").value(EMAIL2))
                .andDo(print());
    }
    
    @Test
    @DisplayName("회원가입 요청 승인")
    void acceptSignup() throws Exception {
        //given
        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .acceptedSignUpIds(List.of(1L, 2L, 3L))
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup/accept", SIGNUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청 거절")
    void acceptSignup1() throws Exception {
        //given
        RejectSignUpRequest request = RejectSignUpRequest.builder()
                .rejectedSignUpIds(List.of(1L, 2L, 3L))
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup/reject", SIGNUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }
}