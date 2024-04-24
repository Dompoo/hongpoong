package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Whitelist;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.WhitelistRepository;
import Dompoo.Hongpoong.request.auth.AcceptEmailRequest;
import Dompoo.Hongpoong.request.auth.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WhitelistRepository whitelistRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        whitelistRepository.deleteAll();
    }

    private static final String USERNAME = "창근";
    private static final String EMAIL = "dompoo@gmail.com";
    private static final String PASSWORD = "1234";

    @Test
    @DisplayName("회원가입")
    void signup() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(true)
                .build());

        SignupRequest request = SignupRequest.builder()
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
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입시 비밀번호와 비밀번호 확인은 일치해야 한다.")
    void signupFail() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(true)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1(PASSWORD)
                .password2("5678")
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
    @DisplayName("회원가입시 유저명은 비어있을 수 없다.")
    void signupFail1() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(true)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email(EMAIL)
                .password1(PASSWORD)
                .password2(PASSWORD)
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
    @DisplayName("회원가입시 유저명은 공백일 수 없다.")
    void signupFail2() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(true)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email(EMAIL)
                .username(" ")
                .password1(PASSWORD)
                .password2(PASSWORD)
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
    @DisplayName("회원가입시 비밀번호는 비어있을 수 없다.")
    void signupFail3() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(true)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password2(PASSWORD)
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
    @DisplayName("회원가입시 비밀번호확인은 비어있을 수 없다.")
    void signupFail4() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(true)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1(PASSWORD)
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
    @DisplayName("회원가입시 유저명은 중복되면 안된다.")
    void signupFail5() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(true)
                .build());

        memberRepository.save(Member.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1("abcd")
                .password2("abcd")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 존재하는 유저명입니다."))
                .andExpect(jsonPath("$.code").value("400"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입시 이메일은 비어있을 수 없다.")
    void signupFail6() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(true)
                .build());

        SignupRequest request = SignupRequest.builder()
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
                .andExpect(jsonPath("$.message").value("[이메일은 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입시 이메일은 이메일 형식으로 입력해야 한다.")
    void signupFail7() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(true)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email("abc")
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
                .andExpect(jsonPath("$.message").value("[이메일 형식으로 입력해주세요.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입시 이메일은 공백일 수 없다.")
    void signupFail8() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(true)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email(" ")
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
                .andExpect(jsonPath("$.message").value("[이메일 형식으로 입력해주세요.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입시 이메일이 화이트리스트에 없으면 가입할 수 없다.")
    void signupFail9() throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
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
                .andExpect(jsonPath("$.message").value("요청하지 않은 유저입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입시 이메일이 승인되지 없으면 가입할 수 없다.")
    void signupFail10() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(false)
                .build());

        SignupRequest request = SignupRequest.builder()
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
                .andExpect(jsonPath("$.message").value("승인되지 않은 유저입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("화이트리스트 추가")
    void addWhiteList() throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(PASSWORD)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("화이트리스트 추가시 이미 있는 이메일은 추가할 수 없다.")
    void addWhiteListFail() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(false)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(PASSWORD)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."))
                .andExpect(jsonPath("$.code").value("400"))
                .andDo(print());
    }

    @Test
    @DisplayName("화이트리스트 승인")
    void acceptEmail() throws Exception {
        //given
        Whitelist whitelist = whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(false)
                .build());

        AcceptEmailRequest request = AcceptEmailRequest.builder()
                .emailId(whitelist.getId())
                .acceptResult(true)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/email/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("화이트리스트 승인시 이미 승인된 이메일은 승인할 수 없다.")
    void acceptEmailFail() throws Exception {
        //given
        Whitelist whitelist = whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(true)
                .build());

        AcceptEmailRequest request = AcceptEmailRequest.builder()
                .emailId(whitelist.getId())
                .acceptResult(true)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/email/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 승인된 이메일입니다."))
                .andExpect(jsonPath("$.code").value("400"))
                .andDo(print());
    }

    @Test
    @DisplayName("화이트리스트 리스트 조회")
    void emailRequestList() throws Exception {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email(EMAIL)
                .isAccepted(false)
                .build());

        whitelistRepository.save(Whitelist.builder()
                .email("yoonH@naver.com")
                .isAccepted(true)
                .build());

        //expected
        mockMvc.perform(get("/auth/email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[0].accepted").value(false))
                .andExpect(jsonPath("$[1].email").value("yoonH@naver.com"))
                .andExpect(jsonPath("$[1].accepted").value(true))
                .andDo(print());
    }
}