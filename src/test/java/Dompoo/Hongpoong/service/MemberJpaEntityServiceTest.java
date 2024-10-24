package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.member.request.MemberEditDto;
import Dompoo.Hongpoong.api.dto.member.request.MemberEditRequest;
import Dompoo.Hongpoong.api.dto.member.request.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.response.MemberStatusResponse;
import Dompoo.Hongpoong.api.service.MemberService;
import Dompoo.Hongpoong.common.exception.impl.EditRoleToAdminException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.Role;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static Dompoo.Hongpoong.domain.enums.Club.AKBAN;
import static Dompoo.Hongpoong.domain.enums.Club.SANTLE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MemberJpaEntityServiceTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private MemberService service;
    @Autowired
    private PasswordEncoder encoder;

    private static final String EMAIL = "dompoo@gmail.com";
    private static final String NAME = "이창근";
    private static final String NICKNAME = "불꽃남자";
    private static final String PASSWORD = "1234";
    private static final Club CLUB = SANTLE;
    private static final Role ROLE = Role.MEMBER;
    private static final Integer ENROLLMENT_NUMBER = 19;
    private static final String PROFILE_IMAGE_URL = "image.com/1";
    
    private static final String EMAIL2 = "yoonH@gmail.com";
    private static final String NAME2 = "새로운이름";
    private static final String PASSWORD2 = "asdf";
    private static final String NICKNAME2 = "물남자";
    private static final Club CLUB2 = AKBAN;
    private static final Integer ENROLLMENT_NUMBER2 = 18;
    private static final String PROFILE_IMAGE_URL2 = "image.com/2";
    
    @AfterEach
    void tearDown() {
        memberJpaRepository.deleteAllInBatch();
    }
    
    @Test
    @DisplayName("회원 리스트 조회")
    void findAllMember() {
        //given
        memberJpaRepository.save(MemberJpaEntity.builder()
                .email(EMAIL)
                .name(NAME)
                .password(PASSWORD)
                .role(Role.LEADER)
                .club(SANTLE)
                .build());
        
        memberJpaRepository.save(MemberJpaEntity.builder()
                .email(EMAIL)
                .name(NAME2)
                .password(PASSWORD2)
                .role(Role.LEADER)
                .club(SANTLE)
                .build());
        
        //when
        List<MemberResponse> list = service.findAllMember();
        
        //then
        assertEquals(2, list.size());
        assertEquals(NAME, list.get(0).getName());
        assertEquals(NAME2, list.get(1).getName());
    }

    @Test
    @DisplayName("로그인 정보")
    void findMyMemberDetail() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(buildMember());

        //when
        MemberStatusResponse response = service.findMyMemberDetail(memberJpaEntity.getId());

        //then
        assertEquals(response.getEmail(), EMAIL);
        assertEquals(response.getName(), NAME);
    }
    
    @Test
    @DisplayName("존재하지 않는 회원의 로그인 정보")
    void findMyMemberDetailFail() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(buildMember());

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class, () ->
                service.findMyMemberDetail(memberJpaEntity.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("회원정보 수정")
    void editMyMember() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(buildMember());
        
        MemberEditDto dto = MemberEditDto.builder()
                .name(NAME2)
                .nickname(NICKNAME2)
                .newPassword(PASSWORD2)
                .profileImageUrl(PROFILE_IMAGE_URL2)
                .enrollmentNumber(ENROLLMENT_NUMBER2)
                .club(CLUB2)
                .build();

        //when
        service.editMyMember(memberJpaEntity.getId(), dto, PASSWORD);

        //then
        assertEquals(memberJpaRepository.findAll().getFirst().getEmail(), "dompoo@gmail.com");
        assertEquals(memberJpaRepository.findAll().getFirst().getName(), NAME2);
        assertTrue(encoder.matches(PASSWORD2, memberJpaRepository.findAll().getFirst().getPassword()));
    }

    @Test
    @DisplayName("존재하지 않는 회원정보 수정 시도")
    void editMyMemberFail2() {
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(buildMember());
        
        MemberEditRequest request = MemberEditRequest.builder()
                .name(NAME2)
                .password(PASSWORD2)
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.editMyMember(memberJpaEntity.getId() + 1, request.toDto(), PASSWORD));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("회원탈퇴")
    void deleteMyMember() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(buildMember());
        
        //when
        service.deleteMemberByAdmin(memberJpaEntity.getId());

        //then
        assertEquals(memberJpaRepository.count(), 0);
    }

    @Test
    @DisplayName("존재하지 않는 회원아이디로 회원탈퇴")
    void deleteMemberByAdminFail1() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(buildMember());
        
        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.deleteMemberByAdmin(memberJpaEntity.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }
    
    @Test
    @DisplayName("내 패 회원 권한 변경")
    void editMemberAuth() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(buildMember());
        MemberJpaEntity target = memberJpaRepository.save(buildMember2());
        
        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .role(Role.LEADER)
                .build();
        
        //when
        service.editMemberAuth(me.getId(), target.getId(), request.toDto());
        
        //then
        assertEquals(memberJpaRepository.findById(target.getId()).get().getRole(), Role.LEADER);
    }
    
    @Test
    @DisplayName("존재하지 않는 회원 권한 변경")
    void editMemberAuthFail() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(buildMember());
        MemberJpaEntity target = memberJpaRepository.save(buildMember2());
        
        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .role(Role.LEADER)
                .build();
        
        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.editMemberAuth(me.getId(), target.getId() + 1, request.toDto()));
        
        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }
    
    @Test
    @DisplayName("의장으로 권한 변경할 수는 없다.")
    void editMemberAuthFail2() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(buildMember());
        MemberJpaEntity target = memberJpaRepository.save(buildMember2());
        
        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .role(Role.ADMIN)
                .build();
        
        //when
        EditRoleToAdminException e = assertThrows(EditRoleToAdminException.class,
                () -> service.editMemberAuth(me.getId(), target.getId(), request.toDto()));
        
        //then
        assertEquals(e.getMessage(), "의장으로 권한을 변경할 수 없습니다.");
        assertEquals(e.statusCode(), "400");
    }
    
    @Test
    @DisplayName("내 패 회원 삭제")
    void deleteMember() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(buildMember());
        MemberJpaEntity target = memberJpaRepository.save(buildMember2());
        
        //when
        service.deleteMember(me.getId(), target.getId());
        
        //then
        assertEquals(memberJpaRepository.findAll().size(), 1);
    }
    
    @Test
    @DisplayName("존재하지 않는 회원 권한 변경")
    void deleteMemberFail() {
        //given
        MemberJpaEntity me = memberJpaRepository.save(buildMember());
        MemberJpaEntity target = memberJpaRepository.save(buildMember2());
        
        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.deleteMember(me.getId(), target.getId() + 1));
        
        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("어드민 회원 권한 변경")
    void editMemberAuthByAdmin() {
        //given
        MemberJpaEntity find = memberJpaRepository.save(buildMember());

        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .role(Role.LEADER)
                .build();

        //when
        service.editMemberAuthByAdmin(find.getId(), request.toDto());

        //then
        assertEquals(memberJpaRepository.findAll().getFirst().getRole().name(), "LEADER");
    }

    @Test
    @DisplayName("어드민 존재하지 않는 회원 권한 변경")
    void editMemberAuthByAdminFail() {
        //given
        MemberJpaEntity find = memberJpaRepository.save(buildMember());

        MemberRoleEditRequest request = MemberRoleEditRequest.builder()
                .role(Role.LEADER)
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.editMemberAuthByAdmin(find.getId() + 1, request.toDto()));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }
    
    @Test
    @DisplayName("어드민 회원 삭제")
    void deleteMemberByAdmin() {
        //given
        MemberJpaEntity target = memberJpaRepository.save(buildMember2());
        
        //when
        service.deleteMemberByAdmin(target.getId());
        
        //then
        assertEquals(memberJpaRepository.findAll().size(), 0);
    }
    
    @Test
    @DisplayName("어드민 존재하지 않는 회원 권한 변경")
    void deleteMemberByAdminFail() {
        //given
        MemberJpaEntity target = memberJpaRepository.save(buildMember2());
        
        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.deleteMemberByAdmin(target.getId() + 1));
        
        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }
    
    private MemberJpaEntity buildMember() {
        return MemberJpaEntity.builder()
                .email(EMAIL)
                .name(NAME)
                .nickname(NICKNAME)
                .password(encoder.encode(PASSWORD))
                .club(CLUB)
                .role(ROLE)
                .enrollmentNumber(ENROLLMENT_NUMBER)
                .profileImageUrl(PROFILE_IMAGE_URL)
                .pushAlarm(true)
                .build();
    }
    
    private MemberJpaEntity buildMember2() {
        return MemberJpaEntity.builder()
                .email(EMAIL2)
                .name(NAME2)
                .nickname(NICKNAME2)
                .password(encoder.encode(PASSWORD2))
                .club(CLUB)
                .role(ROLE)
                .enrollmentNumber(ENROLLMENT_NUMBER2)
                .profileImageUrl(PROFILE_IMAGE_URL2)
                .pushAlarm(true)
                .build();
    }
}