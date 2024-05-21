package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.SignUp;
import Dompoo.Hongpoong.exception.AlreadyExistEmail;
import Dompoo.Hongpoong.exception.PasswordNotSame;
import Dompoo.Hongpoong.exception.SignUpNotFound;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.SignUpRepository;
import Dompoo.Hongpoong.request.auth.AcceptSignUpRequest;
import Dompoo.Hongpoong.request.auth.EmailValidRequest;
import Dompoo.Hongpoong.request.auth.SignUpRequest;
import Dompoo.Hongpoong.response.auth.EmailValidResponse;
import Dompoo.Hongpoong.response.auth.SignUpResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static Dompoo.Hongpoong.domain.Member.Club.HWARANG;
import static Dompoo.Hongpoong.domain.Member.Club.SANTLE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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
    private static final Integer CLUB = 1;
    private static final String NOT_SAME_PASSWORD = "5678";
    private static final String ALREADY_EXIST_EMAIL = "이미 존재하는 이메일입니다.";
    private static final String PASSWORD_NOT_SAME = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        signUpRepository.deleteAll();
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
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
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
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(PASSWORD)
                .club(CLUB)
                .build();

        //when
        service.requestSignup(request);

        //then
        assertEquals(signUpRepository.count(), 1);
        assertEquals(signUpRepository.findAll().getFirst().getUsername(), USERNAME);
        assertTrue(encoder.matches(PASSWORD, signUpRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("회원가입 요청시 비밀번호와 비밀번호확인은 일치해야 한다.")
    void requestSignupFail3() {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(NOT_SAME_PASSWORD)
                .club(CLUB)
                .build();

        //when
        PasswordNotSame e = assertThrows(PasswordNotSame.class, () ->
                service.requestSignup(request));

        //then
        assertEquals(e.statusCode(), "400");
        assertEquals(e.getMessage(), PASSWORD_NOT_SAME);
    }

    @Test
    @DisplayName("회원가입 요청시 이메일은 중복되면 안된다.")
    void requestSignupFail4() {
        //given
        memberRepository.save(Member.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        SignUpRequest request = SignUpRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password1(PASSWORD)
                .password2(PASSWORD)
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
    @DisplayName("회원가입 요청 승인")
    void acceptSignUp() {
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
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .emailId(signUp.getId())
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
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        AcceptSignUpRequest request = AcceptSignUpRequest.builder()
                .emailId(signUp.getId() + 1)
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
    void getSignUpList() {
        //given
        signUpRepository.save(SignUp.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());

        signUpRepository.save(SignUp.builder()
                .email("yoonH@naver.com")
                .username("윤호")
                .password("qwer")
                .club(HWARANG)
                .build());

        //when
        List<SignUpResponse> response = service.getSignUp();

        //then
        assertEquals(2, response.size());
        assertEquals(EMAIL, response.getFirst().getEmail());
        assertEquals("yoonH@naver.com", response.get(1).getEmail());
    }
}