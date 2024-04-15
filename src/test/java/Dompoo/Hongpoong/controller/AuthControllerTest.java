package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.repository.MemberRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MemberRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void signup() throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .username("창근")
                .password1("1234")
                .password2("1234")
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
        SignupRequest request = SignupRequest.builder()
                .username("창근")
                .password1("1234")
                .password2("5678")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."))
                .andExpect(jsonPath("$.code").value("400"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입시 유저명은 비어있을 수 없다.")
    void signupFail1() throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .password1("1234")
                .password2("1234")
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
        SignupRequest request = SignupRequest.builder()
                .username(" ")
                .password1("1234")
                .password2("1234")
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
        SignupRequest request = SignupRequest.builder()
                .username("창근")
                .password2("1234")
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
        SignupRequest request = SignupRequest.builder()
                .username("창근")
                .password1("1234")
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
        repository.save(Member.builder()
                .username("창근")
                .password("1234")
                .build());

        SignupRequest request = SignupRequest.builder()
                .username("창근")
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
}