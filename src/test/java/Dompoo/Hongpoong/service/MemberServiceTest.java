package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.exception.MemberNotFound;
import Dompoo.Hongpoong.exception.PasswordNotSame;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.request.member.MemberEditRequest;
import Dompoo.Hongpoong.request.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.response.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService service;
    @Autowired
    private PasswordEncoder encoder;

    private static final String EMAIL = "dompoo@gmail.com";
    private static final String USERNAME = "창근";
    private static final String PASSWORD = "1234";
    private static final String NEW_USERNAME = "새로운이름";
    private static final String NEW_PASSWORD = "asdf";

    private Member member;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        member = memberRepository.save(Member.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build());
    }

    @Test
    @DisplayName("로그인 정보")
    void getStatus() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email(EMAIL)
                .username(NEW_USERNAME)
                .password(NEW_PASSWORD)
                .build());

        //when
        MemberResponse response = service.getStatus(member.getId());

        //then
        assertEquals(response.getEmail(), EMAIL);
        assertEquals(response.getUsername(), NEW_USERNAME);
        assertEquals(response.getPassword(), NEW_PASSWORD);
    }

    @Test
    @DisplayName("존재하지 않는 회원의 로그인 정보")
    void getStatusFail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email(EMAIL)
                .username(NEW_USERNAME)
                .password(NEW_PASSWORD)
                .build());

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class, () ->
                service.getStatus(member.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("멤버 정보 전체 수정")
    void editMember1() {
        //given
        MemberEditRequest request = MemberEditRequest.builder()
                .username(NEW_USERNAME)
                .password1(NEW_PASSWORD)
                .password2(NEW_PASSWORD)
                .build();

        //when
        service.editMember(member.getId(), request);

        //then
        assertEquals(memberRepository.findAll().getFirst().getEmail(), "dompoo@gmail.com");
        assertEquals(memberRepository.findAll().getFirst().getUsername(), NEW_USERNAME);
        assertTrue(encoder.matches(NEW_PASSWORD, memberRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("멤버 정보 일부 수정")
    void editMember2() {
        //given
        MemberEditRequest request = MemberEditRequest.builder()
                .password1(NEW_PASSWORD)
                .password2(NEW_PASSWORD)
                .build();

        //when
        service.editMember(member.getId(), request);

        //then
        assertEquals(memberRepository.findAll().getFirst().getEmail(), "dompoo@gmail.com");
        assertEquals(memberRepository.findAll().getFirst().getUsername(), "창근");
        assertTrue(encoder.matches(NEW_PASSWORD, memberRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("멤버 정보 수정시 비밀번호와 비밀번호 확인은 일치해야 한다.")
    void editMemberFail1() {
        MemberEditRequest request = MemberEditRequest.builder()
                .username(NEW_USERNAME)
                .password1(NEW_PASSWORD)
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
        MemberEditRequest request = MemberEditRequest.builder()
                .username(NEW_USERNAME)
                .password1(NEW_PASSWORD)
                .password2(NEW_PASSWORD)
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
        //when
        service.deleteMember(member.getId());

        //then
        assertEquals(memberRepository.count(), 0);
    }

    @Test
    @DisplayName("존재하지 않는 회원아이디로 회원탈퇴")
    void deleteMemberFail1() {
        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.deleteMember(member.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("회원 리스트 조회")
    void getList() {
        //given
        memberRepository.save(Member.builder()
                .email(EMAIL)
                .username(NEW_USERNAME)
                .password(NEW_PASSWORD)
                .build());

        //when
        List<MemberResponse> list = service.getList();

        //then
        assertEquals(2, list.size());
        assertEquals(USERNAME, list.get(0).getUsername());
        assertEquals(PASSWORD, list.get(0).getPassword());
        assertEquals(NEW_USERNAME, list.get(1).getUsername());
        assertEquals(NEW_PASSWORD, list.get(1).getPassword());
    }

    @Test
    @DisplayName("회원 권한 변경")
    void editRole() {
        //given
        Member find = memberRepository.findAll().getFirst();

        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .isAdmin(true)
                .build();

        //when
        service.editRole(find.getId(), request);

        //then
        assertEquals(memberRepository.findAll().getFirst().getRole(), "ROLE_ADMIN");
    }

    @Test
    @DisplayName("존재하지 않는 회원 권한 변경")
    void editRoleFail() {
        //given
        Member find = memberRepository.findAll().getFirst();

        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .isAdmin(true)
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.editRole(find.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }
}