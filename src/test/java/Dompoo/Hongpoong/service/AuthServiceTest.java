package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.auth.request.AcceptSignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.request.EmailValidRequest;
import Dompoo.Hongpoong.api.dto.auth.request.SignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.response.EmailValidResponse;
import Dompoo.Hongpoong.api.dto.auth.response.SignUpResponse;
import Dompoo.Hongpoong.api.service.AuthService;
import Dompoo.Hongpoong.common.exception.impl.AlreadyExistEmail;
import Dompoo.Hongpoong.common.exception.impl.SignUpNotFound;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.SignUp;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.SignUpRepository;
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
                .club(CLUB.korName)
                .build();

        //when
        service.requestSignup(request);

        //then
        assertEquals(signUpRepository.count(), 1);
        assertEquals(signUpRepository.findAll().getFirst().getName(), USERNAME);
        assertTrue(encoder.matches(PASSWORD, signUpRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("회원가입 요청시 이메일은 중복되면 안된다.")
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
                .club(CLUB.korName)
                .build();

        //when
        AlreadyExistEmail e = assertThrows(AlreadyExistEmail.class, () ->
                service.requestSignup(request));

        //then
        assertEquals(e.statusCode(), "400");
        assertEquals(e.getMessage(), ALREADY_EXIST_EMAIL);
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
                .signupId(signUp.getId())
                .acceptResult(true)
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

        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .signupId(signUp.getId())
                .acceptResult(false)
                .build();

        //when
        service.acceptSignUp(request);

        //then
        assertEquals(0, signUpRepository.findAll().size());
        assertEquals(0, memberRepository.findAll().size());
    }

    @Test
    @DisplayName("존재하지 않는 회원가입 요청 승인")
    void acceptSignUpFail() {
        //given
        SignUp signUp = signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .signupId(signUp.getId() + 1)
                .acceptResult(true)
                .build();

        //when
        SignUpNotFound e = assertThrows(SignUpNotFound.class, () ->
                service.acceptSignUp(request));

        //then
        assertEquals("존재하지 않는 회원가입 요청입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
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