package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.request.auth.AcceptSignUpRequest;
import Dompoo.Hongpoong.api.dto.request.auth.EmailValidRequest;
import Dompoo.Hongpoong.api.dto.request.auth.SignUpRequest;
import Dompoo.Hongpoong.config.WithMockMember;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.SignUp;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.SignUpRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static Dompoo.Hongpoong.domain.enums.Club.SANTLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SignUpRepository signUpRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        signUpRepository.deleteAll();
    }

    private static final String USERNAME = "창근";
    private static final String EMAIL = "dompoo@gmail.com";
    private static final String PASSWORD = "1234";
    private static final Integer CLUB = 1;

    @Test
    @DisplayName("이메일 유효성 검사 - 성공")
    void checkEmail() throws Exception {
        //given
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
    @DisplayName("이메일 유효성 검사 - 실패")
    void checkEmailFail() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
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
                .andExpect(jsonPath("$.valid").value(false))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청")
    void requestSignup() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(PASSWORD)
                .club(CLUB)
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
    @DisplayName("회원가입 요청시 비밀번호와 비밀번호 확인은 일치해야 한다.")
    void requestSignupFail() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1(PASSWORD)
                .password2("5678")
                .club(CLUB)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호와 비밀번호 확인이 일치하지 않습니다."))
                .andExpect(jsonPath("$.code").value("400"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청시 유저명은 비어있을 수 없다.")
    void requestSignupFail1() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .password1(PASSWORD)
                .password2(PASSWORD)
                .club(CLUB)
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
                .username(" ")
                .password1(PASSWORD)
                .password2(PASSWORD)
                .club(CLUB)
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
                .username(USERNAME)
                .password2(PASSWORD)
                .club(CLUB)
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
    @DisplayName("회원가입 요청시 비밀번호확인은 비어있을 수 없다.")
    void requestSignupFail4() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1(PASSWORD)
                .club(CLUB)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[비밀번호확인은 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청시 이메일은 비어있을 수 없다.")
    void requestSignupFail6() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(PASSWORD)
                .club(CLUB)
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
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(PASSWORD)
                .club(CLUB)
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
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(PASSWORD)
                .club(CLUB)
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
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(PASSWORD)
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
    @DisplayName("회원가입 요청시 이메일이 요청 리스트와 중복되면 가입할 수 없다.")
    void requestSignupFail9() throws Exception {
        //given
        signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .username("username")
                .password("1234")
                .club(SANTLE)
                .build());

        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(PASSWORD)
                .club(CLUB)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청시 이메일이 회원 리스트와 중복되면 가입할 수 없다.")
    void requestSignupFail12() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .email(EMAIL)
                .username("username")
                .password("1234")
                .club(SANTLE)
                .build());

        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(PASSWORD)
                .club(CLUB)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청 승인")
    @WithMockMember(role = "ROLE_ADMIN")
    void acceptSignup() throws Exception {
        //given
        SignUp signUp = signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .emailId(signUp.getId())
                .acceptResult(true)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청 거절")
    @WithMockMember(role = "ROLE_ADMIN")
    void acceptSignup1() throws Exception {
        //given
        SignUp signUp = signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .emailId(signUp.getId())
                .acceptResult(false)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원은 회원가입 요청을 승인할 수 없다.")
    @WithMockMember
    void acceptSignupFail1() throws Exception {
        //given
        SignUp signUp = signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .emailId(signUp.getId())
                .acceptResult(true)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("[인증오류] 권한이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청 리스트 조회")
    @WithMockMember(role = "ROLE_ADMIN")
    void emailRequestList() throws Exception {
        //given
        signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        signUpRepository.save(SignUp.builder()
                .email("yoonH@naver.com")
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        //expected
        mockMvc.perform(get("/auth/signup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[1].email").value("yoonH@naver.com"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원은 회원가입 요청 리스트를 조회할 수 없다.")
    @WithMockMember
    void emailRequestListFail() throws Exception {
        //given
        signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        signUpRepository.save(SignUp.builder()
                .email("yoonH@naver.com")
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        //expected
        mockMvc.perform(get("/auth/signup"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("[인증오류] 권한이 없습니다."))
                .andDo(print());
    }
}