package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Whitelist;
import Dompoo.Hongpoong.exception.*;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.WhitelistRepository;
import Dompoo.Hongpoong.request.auth.AcceptEmailRequest;
import Dompoo.Hongpoong.request.auth.AddEmailRequest;
import Dompoo.Hongpoong.request.auth.SignupRequest;
import Dompoo.Hongpoong.response.EmailResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService service;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WhitelistRepository whitelistRepository;
    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        whitelistRepository.deleteAll();
    }

    @Test
    @DisplayName("화이트리스트 추가")
    void addWhiteList() {
        //given
        AddEmailRequest request = AddEmailRequest.builder()
                .email("dompoo@gmail.com")
                .build();
        //when
        service.addWhiteList(request);

        //then
        assertEquals(whitelistRepository.count(), 1);
        assertEquals(whitelistRepository.findAll().getFirst().getEmail(), "dompoo@gmail.com");
        assertEquals(whitelistRepository.findAll().getFirst().getIsAccepted(), false);
    }

    @Test
    @DisplayName("화이트리스트 추가시 이메일은 중복되면 안된다.")
    void addWhiteListFail() {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email("dompoo@gmail.com")
                .isAccepted(false)
                .build());

        AddEmailRequest request = AddEmailRequest.builder()
                .email("dompoo@gmail.com")
                .build();
        //when
        AlreadyExistsEmail e = assertThrows(AlreadyExistsEmail.class,
                () -> service.addWhiteList(request));

        //then
        assertEquals(e.getMessage(), "이미 존재하는 이메일입니다.");
        assertEquals(e.statusCode(), "400");
    }

    @Test
    @DisplayName("화이트리스트 조회")
    void getWhiteList() {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email("dompoo@gmail.com")
                .isAccepted(false)
                .build());

        whitelistRepository.save(Whitelist.builder()
                .email("dompoo2@naver.com")
                .isAccepted(true)
                .build());
        //when
        List<EmailResponse> whiteList = service.getWhiteList();

        //then
        assertEquals(whiteList.getFirst().getEmail(), "dompoo@gmail.com");
        assertFalse(whiteList.getFirst().isAccepted());
        assertEquals(whiteList.get(1).getEmail(), "dompoo2@naver.com");
        assertTrue(whiteList.get(1).isAccepted());
    }

    @Test
    @DisplayName("화이트리스트 승인")
    void acceptWhiteList() {
        //given
        Whitelist whitelist = whitelistRepository.save(Whitelist.builder()
                .email("dompoo@gmail.com")
                .isAccepted(false)
                .build());

        AcceptEmailRequest request = AcceptEmailRequest.builder()
                .emailId(whitelist.getId())
                .acceptResult(true)
                .build();
        //when
        service.acceptWhiteList(request);

        //then
        assertEquals(whitelistRepository.count(), 1);
        assertEquals(whitelistRepository.findAll().getFirst().getEmail(), "dompoo@gmail.com");
        assertEquals(whitelistRepository.findAll().getFirst().getIsAccepted(), true);
    }

    @Test
    @DisplayName("화이트리스트 거절")
    void notAcceptWhiteList() {
        //given
        Whitelist whitelist = whitelistRepository.save(Whitelist.builder()
                .email("dompoo@gmail.com")
                .isAccepted(false)
                .build());

        AcceptEmailRequest request = AcceptEmailRequest.builder()
                .emailId(whitelist.getId())
                .acceptResult(false)
                .build();
        //when
        service.acceptWhiteList(request);

        //then
        assertEquals(whitelistRepository.count(), 0);
    }

    @Test
    @DisplayName("회원가입")
    void signup() {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email("dompoo@gmail.com")
                .isAccepted(true)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password1("1234")
                .password2("1234")
                .build();

        //when
        service.signup(request);

        //then
        assertEquals(memberRepository.count(), 1);
        assertEquals(memberRepository.findAll().getFirst().getUsername(), "창근");
        assertTrue(encoder.matches("1234", memberRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("회원가입시 화이트리스트에 올라간 이메일만 가능하다.")
    void signupFail1() {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email("dompoo@gmail.com")
                .isAccepted(true)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email("notdompoo@gmail.com")
                .username("창근")
                .password1("1234")
                .password2("1234")
                .build();

        //when
        NotInWhitelist e = assertThrows(NotInWhitelist.class,
                () -> service.signup(request));

        //then
        assertEquals(e.getMessage(), "요청하지 않은 유저입니다.");
        assertEquals(e.statusCode(), "400");
    }

    @Test
    @DisplayName("회원가입시 화이트리스트에서 승인된 이메일만 가능하다.")
    void signupFail2() {
        //given
        whitelistRepository.save(Whitelist.builder()
                .email("dompoo@gmail.com")
                .isAccepted(false)
                .build());

        SignupRequest request = SignupRequest.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password1("1234")
                .password2("1234")
                .build();

        //when
        NotAcceptedMember e = assertThrows(NotAcceptedMember.class,
                () -> service.signup(request));

        //then
        assertEquals(e.getMessage(), "승인되지 않은 유저입니다.");
        assertEquals(e.statusCode(), "400");
    }

    @Test
    @DisplayName("회원가입시 비밀번호와 비밀번호확인은 일치해야 한다.")
    void signupFail3() {
        //given
        SignupRequest request = SignupRequest.builder()
                .username("창근")
                .password1("1234")
                .password2("5678")
                .build();

        //when
        PasswordNotSame e = assertThrows(PasswordNotSame.class, () ->
                service.signup(request));

        //then
        assertEquals(e.statusCode(), "400");
        assertEquals(e.getMessage(), "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }

    @Test
    @DisplayName("회원가입시 유저명은 중복되면 안된다.")
    void signupFail4() {
        //given
        memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        SignupRequest request = SignupRequest.builder()
                .email("dompoo2@gmail.com")
                .username("창근")
                .password1("abcd")
                .password2("abcd")
                .build();

        //when
        AlreadyExistsUsername e = assertThrows(AlreadyExistsUsername.class, () ->
                service.signup(request));

        //then
        assertEquals(e.statusCode(), "400");
        assertEquals(e.getMessage(), "이미 존재하는 유저명입니다.");
    }

}