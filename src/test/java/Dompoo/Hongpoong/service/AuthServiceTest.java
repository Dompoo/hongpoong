package Dompoo.Hongpoong.service;

import static Dompoo.Hongpoong.domain.enums.Club.HWARANG;
import static Dompoo.Hongpoong.domain.enums.Club.SANTLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Dompoo.Hongpoong.api.dto.auth.request.AcceptSignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.request.EmailValidRequest;
import Dompoo.Hongpoong.api.dto.auth.request.LoginRequest;
import Dompoo.Hongpoong.api.dto.auth.request.RejectSignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.request.SignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.response.EmailValidResponse;
import Dompoo.Hongpoong.api.dto.auth.response.LoginResponse;
import Dompoo.Hongpoong.api.dto.auth.response.SignUpResponse;
import Dompoo.Hongpoong.api.service.AuthService;
import Dompoo.Hongpoong.common.exception.impl.AlreadyExistEmail;
import Dompoo.Hongpoong.common.exception.impl.LoginFailException;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.SignUp;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.SignUpRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService service;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SignUpRepository signUpRepository;
    @Autowired
    private PasswordEncoder encoder;

    private static final String EMAIL = "dompoo@gmail.com";
    private static final String USERNAME = "창근";
    private static final String PASSWORD = "1234";
    private static final Club CLUB = SANTLE;
    private static final String ALREADY_EXIST_EMAIL = "이미 존재하는 이메일입니다.";

    @AfterEach
    void setUp() {
        signUpRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("이메일 유효성 검사 - 성공")
    void checkEmail() {
        //given
        EmailValidRequest request = EmailValidRequest.builder()
                .email(EMAIL)
                .build();

        //when
        EmailValidResponse response = service.checkEmailValid(request);

        //then
        assertTrue(response.getValid());
    }

    @Test
    @DisplayName("이메일 유효성 검사 - 실패")
    void checkEmailFail() {
        //given
        memberRepository.save(Member.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(CLUB)
                .build());

        EmailValidRequest request = EmailValidRequest.builder()
                .email(EMAIL)
                .build();

        //when
        EmailValidResponse response = service.checkEmailValid(request);

        //then
        assertFalse(response.getValid());
    }

    @Test
    @DisplayName("회원가입 요청")
    void requestSignup() {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(CLUB)
                .build();

        //when
        service.requestSignup(request);

        //then
        assertEquals(signUpRepository.count(), 1);
        assertEquals(signUpRepository.findAll().getFirst().getName(), USERNAME);
        assertTrue(encoder.matches(PASSWORD, signUpRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("회원가입 요청시 이메일은 기존 회원과 중복되면 안된다.")
    void requestSignupFail4() {
        //given
        memberRepository.save(Member.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(CLUB)
                .build();

        //when
        AlreadyExistEmail e = assertThrows(AlreadyExistEmail.class, () ->
                service.requestSignup(request));

        //then
        assertEquals(e.statusCode(), "400");
        assertEquals(e.getMessage(), ALREADY_EXIST_EMAIL);
    }
    
    @Test
    @DisplayName("회원가입 요청시 이메일은 기존 회원가입 요청과 중복되면 안된다.")
    void requestSignupFail5() {
        //given
        signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());
        
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(CLUB)
                .build();
        
        //when
        AlreadyExistEmail e = assertThrows(AlreadyExistEmail.class, () ->
                service.requestSignup(request));
        
        //then
        assertEquals(e.statusCode(), "400");
        assertEquals(e.getMessage(), ALREADY_EXIST_EMAIL);
    }
    
    @Test
    @DisplayName("로그인")
    void login() {
        //given
        memberRepository.save(Member.builder()
                .email(EMAIL)
                .password(encoder.encode(PASSWORD))
                .build());
        
        LoginRequest request = LoginRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();
        
        //when
        LoginResponse response = service.login(request);
        
        //then
        assertNotNull(response.getToken());
    }
    
    @Test
    @DisplayName("존재하지 않는 회원 로그인")
    void loginFail() {
        //given
        memberRepository.save(Member.builder()
                .email(EMAIL)
                .password(encoder.encode(PASSWORD))
                .build());
        
        LoginRequest request = LoginRequest.builder()
                .email("잘못된 이메일")
                .password(PASSWORD)
                .build();
        
        //when
        LoginFailException e = assertThrows(LoginFailException.class, () -> service.login(request));
        
        //then
        assertEquals("400", e.statusCode());
        assertEquals("이메일 또는 비밀번호가 잘못되었습니다.", e.getMessage());
    }

    @Test
    @DisplayName("회원가입 요청 승인")
    void acceptSignUp() {
        //given
        SignUp signUp = signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .acceptedSignUpIds(List.of(signUp.getId()))
                .build();

        //when
        service.acceptSignUp(request);

        //then
        assertEquals(0, signUpRepository.findAll().size());
        assertEquals(1, memberRepository.findAll().size());
        assertEquals(EMAIL, memberRepository.findAll().getFirst().getEmail());
    }

    @Test
    @DisplayName("회원가입 요청 거절")
    void refuseSignUp() {
        //given
        SignUp signUp = signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        RejectSignUpRequest request = RejectSignUpRequest.builder()
                .rejectedSignUpIds(List.of(signUp.getId()))
                .build();

        //when
        service.rejectSignUp(request);

        //then
        assertEquals(0, signUpRepository.findAll().size());
        assertEquals(0, memberRepository.findAll().size());
    }

    @Test
    @DisplayName("회원가입 요청 리스트 조회")
    void findAllSignup() {
        //given
        signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        signUpRepository.save(SignUp.builder()
                .email("yoonH@naver.com")
                .name("윤호")
                .password("qwer")
                .club(HWARANG)
                .build());

        //when
        List<SignUpResponse> response = service.findAllSignup();

        //then
        assertEquals(2, response.size());
        assertEquals(EMAIL, response.getFirst().getEmail());
        assertEquals("yoonH@naver.com", response.get(1).getEmail());
    }
}