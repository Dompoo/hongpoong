package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.member.MemberEditRequest;
import Dompoo.Hongpoong.api.dto.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.response.MemberStatusResponse;
import Dompoo.Hongpoong.api.service.MemberService;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.enums.Role;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static Dompoo.Hongpoong.domain.enums.Club.SANTLE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
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
        member = memberRepository.save(Member.builder()
                .email(EMAIL)
                .name(USERNAME)
                .password(PASSWORD)
                .club(SANTLE)
                .build());
    }
    
    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("로그인 정보")
    void findMyMemberDetail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email(EMAIL)
                .name(NEW_USERNAME)
                .password(NEW_PASSWORD)
                .club(SANTLE)
                .build());

        //when
        MemberStatusResponse response = service.findMyMemberDetail(member.getId());

        //then
        assertEquals(response.getEmail(), EMAIL);
        assertEquals(response.getUsername(), NEW_USERNAME);
        assertEquals(response.getPassword(), NEW_PASSWORD);
    }

    @Test
    @DisplayName("존재하지 않는 회원의 로그인 정보")
    void findMyMemberDetailFail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email(EMAIL)
                .name(NEW_USERNAME)
                .password(NEW_PASSWORD)
                .build());

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class, () ->
                service.findMyMemberDetail(member.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("멤버 정보 전체 수정")
    void editMyMember1() {
        //given
        MemberEditRequest request = MemberEditRequest.builder()
                .username(NEW_USERNAME)
                .password(NEW_PASSWORD)
                .build();

        //when
        service.editMyMember(member.getId(), request.toDto());

        //then
        assertEquals(memberRepository.findAll().getFirst().getEmail(), "dompoo@gmail.com");
        assertEquals(memberRepository.findAll().getFirst().getName(), NEW_USERNAME);
        assertTrue(encoder.matches(NEW_PASSWORD, memberRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("멤버 정보 일부 수정")
    void editMyMember2() {
        //given
        MemberEditRequest request = MemberEditRequest.builder()
                .password(NEW_PASSWORD)
                .build();

        //when
        service.editMyMember(member.getId(), request.toDto());

        //then
        assertEquals(memberRepository.findAll().getFirst().getEmail(), "dompoo@gmail.com");
        assertEquals(memberRepository.findAll().getFirst().getName(), "창근");
        assertTrue(encoder.matches(NEW_PASSWORD, memberRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("존재하는 아이디로 멤버 수정 시도")
    void editMyMemberFail2() {
        MemberEditRequest request = MemberEditRequest.builder()
                .username(NEW_USERNAME)
                .password(NEW_PASSWORD)
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.editMyMember(member.getId() + 1, request.toDto()));

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
    void findAllMember() {
        //given
        memberRepository.save(Member.builder()
                .email(EMAIL)
                .name(NEW_USERNAME)
                .password(NEW_PASSWORD)
                .club(SANTLE)
                .build());

        //when
        List<MemberResponse> list = service.findAllMember();

        //then
        assertEquals(2, list.size());
        assertEquals(USERNAME, list.get(0).getName());
        assertEquals(NEW_USERNAME, list.get(1).getName());
    }

    @Test
    @DisplayName("회원 권한 변경")
    void editMemberAuth() {
        //given
        Member find = memberRepository.findAll().getFirst();

        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .role(Role.ROLE_LEADER)
                .build();

        //when
        service.editMemberAuth(find.getId(), request);

        //then
        assertEquals(memberRepository.findAll().getFirst().getRole().name(), "ROLE_LEADER");
    }

    @Test
    @DisplayName("존재하지 않는 회원 권한 변경")
    void editMemberAuthFail() {
        //given
        Member find = memberRepository.findAll().getFirst();

        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .role(Role.ROLE_LEADER)
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.editMemberAuth(find.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }
}