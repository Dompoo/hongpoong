package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.exception.MemberNotFound;
import Dompoo.Hongpoong.exception.PasswordNotSame;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.request.Member.MemberEditRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService service;
    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("멤버 정보 전체 수정")
    void editMember1() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        MemberEditRequest request = MemberEditRequest.builder()
                .username("새로운이름")
                .password1("asdf")
                .password2("asdf")
                .build();

        //when
        service.editMember(member.getId(), request);

        //then
        assertEquals(memberRepository.findAll().getFirst().getEmail(), "dompoo@gmail.com");
        assertEquals(memberRepository.findAll().getFirst().getUsername(), "새로운이름");
        assertTrue(encoder.matches("asdf", memberRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("멤버 정보 일부 수정")
    void editMember2() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        MemberEditRequest request = MemberEditRequest.builder()
                .password1("asdf")
                .password2("asdf")
                .build();

        //when
        service.editMember(member.getId(), request);

        //then
        assertEquals(memberRepository.findAll().getFirst().getEmail(), "dompoo@gmail.com");
        assertEquals(memberRepository.findAll().getFirst().getUsername(), "창근");
        assertTrue(encoder.matches("asdf", memberRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("멤버 정보 수정시 비밀번호와 비밀번호 확인은 일치해야 한다.")
    void editMemberFail1() {
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        MemberEditRequest request = MemberEditRequest.builder()
                .username("새로운이름")
                .password1("asdf")
                .password2("qwer")
                .build();

        //when
        PasswordNotSame e = assertThrows(PasswordNotSame.class,
                () -> service.editMember(member.getId(), request));

        //then
        assertEquals(e.getMessage(), "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        assertEquals(e.statusCode(), "400");
    }

    @Test
    @DisplayName("존재하는 아이디로 멤버 수정 시도")
    void editMemberFail2() {
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        MemberEditRequest request = MemberEditRequest.builder()
                .username("새로운이름")
                .password1("asdf")
                .password2("asdf")
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.editMember(member.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("회원탈퇴")
    void deleteMember() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        //when
        service.deleteMember(member.getId());

        //then
        assertEquals(memberRepository.count(), 0);
    }

    @Test
    @DisplayName("존재하지 않는 회원아이디로 회원탈퇴")
    void deleteMemberFail1() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("창근")
                .password("1234")
                .build());

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.deleteMember(member.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }
}