package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.auth.request.AcceptSignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.request.EmailValidRequest;
import Dompoo.Hongpoong.api.dto.auth.request.LoginRequest;
import Dompoo.Hongpoong.api.dto.auth.request.SignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.response.EmailValidResponse;
import Dompoo.Hongpoong.api.dto.auth.response.LoginResponse;
import Dompoo.Hongpoong.api.dto.auth.response.SignUpResponse;
import Dompoo.Hongpoong.api.service.AuthService;
import Dompoo.Hongpoong.common.exception.impl.AlreadyExistEmail;
import Dompoo.Hongpoong.common.exception.impl.LoginFailException;
import Dompoo.Hongpoong.common.exception.impl.SignUpNotFound;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.SignUpJpaEntity;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.SignUpJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static Dompoo.Hongpoong.domain.enums.Club.HWARANG;
import static Dompoo.Hongpoong.domain.enums.Club.SANTLE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService service;
    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private SignUpJpaRepository signUpJpaRepository;
    @Autowired
    private PasswordEncoder encoder;

    private static final String EMAIL = "dompoo@gmail.com";
    private static final String USERNAME = "창근";
    private static final String PASSWORD = "1234";
    private static final Club CLUB = SANTLE;
    private static final String ALREADY_EXIST_EMAIL = "이미 존재하는 이메일입니다.";

    @AfterEach
    void setUp() {
        signUpJpaRepository.deleteAllInBatch();
        memberJpaRepository.deleteAllInBatch();
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
        memberJpaRepository.save(MemberJpaEntity.builder()
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
        assertEquals(signUpJpaRepository.count(), 1);
        assertEquals(signUpJpaRepository.findAll().getFirst().getName(), USERNAME);
        assertTrue(encoder.matches(PASSWORD, signUpJpaRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("회원가입 요청시 이메일은 기존 회원과 중복되면 안된다.")
    void requestSignupFail4() {
        //given
        memberJpaRepository.save(MemberJpaEntity.builder()
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
        signUpJpaRepository.save(SignUpJpaEntity.builder()
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
        memberJpaRepository.save(MemberJpaEntity.builder()
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
        memberJpaRepository.save(MemberJpaEntity.builder()
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
        SignUpJpaEntity signUpJpaEntity = signUpJpaRepository.save(SignUpJpaEntity.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .acceptResult(true)
                .build();

        //when
        service.acceptSignUp(signUpJpaEntity.getId(), request);

        //then
        assertEquals(0, signUpJpaRepository.findAll().size());
        assertEquals(1, memberJpaRepository.findAll().size());
        assertEquals(EMAIL, memberJpaRepository.findAll().getFirst().getEmail());
    }

    @Test
    @DisplayName("회원가입 요청 거절")
    void refuseSignUp() {
        //given
        SignUpJpaEntity signUpJpaEntity = signUpJpaRepository.save(SignUpJpaEntity.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .acceptResult(false)
                .build();

        //when
        service.acceptSignUp(signUpJpaEntity.getId(), request);

        //then
        assertEquals(0, signUpJpaRepository.findAll().size());
        assertEquals(0, memberJpaRepository.findAll().size());
    }

    @Test
    @DisplayName("존재하지 않는 회원가입 요청 승인")
    void acceptSignUpFail() {
        //given
        SignUpJpaEntity signUpJpaEntity = signUpJpaRepository.save(SignUpJpaEntity.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .acceptResult(true)
                .build();

        //when
        SignUpNotFound e = assertThrows(SignUpNotFound.class, () ->
                service.acceptSignUp(signUpJpaEntity.getId() + 1, request));

        //then
        assertEquals("존재하지 않는 회원가입 요청입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
    }

    @Test
    @DisplayName("회원가입 요청 리스트 조회")
    void findAllSignup() {
        //given
        signUpJpaRepository.save(SignUpJpaEntity.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        signUpJpaRepository.save(SignUpJpaEntity.builder()
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