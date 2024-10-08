package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.info.request.InfoCreateRequest;
import Dompoo.Hongpoong.api.dto.info.request.InfoEditRequest;
import Dompoo.Hongpoong.api.dto.info.response.InfoDetailResponse;
import Dompoo.Hongpoong.api.dto.info.response.InfoResponse;
import Dompoo.Hongpoong.api.service.InfoService;
import Dompoo.Hongpoong.common.exception.impl.InfoNotFound;
import Dompoo.Hongpoong.domain.entity.Info;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.Role;
import Dompoo.Hongpoong.domain.repository.InfoRepository;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class InfoServiceTest {
    
    @Autowired
    private InfoService service;

    @Autowired
    private InfoRepository infoRepository;
    
    @Autowired
    private MemberRepository memberRepository;

    //info 서비스의 여러 메서드를 테스트하는 코드 작성

    private static final String TITLE = "공지사항 제목";
    private static final String CONTENT = "공지사항 내용";
    private static final String NEW_TITLE = "새로운 공지사항 제목";
    private static final String NEW_CONTENT = "새로운 공지사항 내용";
    
    @AfterEach
    void tearDown() {
        infoRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    //공지사항 추가 테스트
    @Test
    @DisplayName("공지사항 추가")
    void addOne() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("이창근")
                .nickname("불꽃남자")
                .role(Role.LEADER)
                .enrollmentNumber(19)
                .build());
        
        InfoCreateRequest request = InfoCreateRequest.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
        
        LocalDateTime now = LocalDateTime.of(2000, 5, 17, 11, 23, 30);
        
        //when
        service.createInfo(member.getId(), request, now);

        //then
        assertEquals(1, infoRepository.count());
        assertEquals(TITLE, infoRepository.findAll().get(0).getTitle());
        assertEquals(CONTENT, infoRepository.findAll().get(0).getContent());
    }

    //공지사항 전체 조회 테스트
    @Test
    @DisplayName("공지사항 전체 조회")
    void findAll() {
        //given
        infoRepository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        infoRepository.save(Info.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build());

        //when
        List<InfoResponse> list = service.findAllInfo();

        //then
        assertEquals(2, list.size());
        assertEquals(TITLE, list.get(0).getTitle());
        assertEquals(NEW_TITLE, list.get(1).getTitle());
    }

    //공지사항 상세 조회 테스트
    @Test
    @DisplayName("공지사항 상세 조회")
    void findOne() {
        //given
        Info save = infoRepository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        //when
        InfoDetailResponse info = service.findInfoDetail(save.getId());

        //then
        assertEquals(save.getId(), info.getInfoId());
        assertEquals(TITLE, info.getTitle());
        assertEquals(CONTENT, info.getContent());
    }

    //공지사항 상세 조회 실패 테스트
    @Test
    @DisplayName("존재하지 않는 공지사항 상세 조회")
    void findOneFail() {
        //given
        Info save = infoRepository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        //when
        InfoNotFound e = assertThrows(InfoNotFound.class, () -> service.findInfoDetail(save.getId() + 1));

        //then
        assertEquals("존재하지 않는 공지사항입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
    }

    //공지사항 수정 테스트
    @Test
    @DisplayName("공지사항 수정")
    void edit() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("이창근")
                .nickname("불꽃남자")
                .role(Role.LEADER)
                .enrollmentNumber(19)
                .club(Club.SANTLE)
                .build());
        
        Info info = infoRepository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .member(member)
                .build());

        InfoEditRequest request = InfoEditRequest.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build();

        //when
        service.editInfo(member.getId(), info.getId(), request.toDto());

        //then
        assertEquals(NEW_TITLE, infoRepository.findById(info.getId()).get().getTitle());
        assertEquals(NEW_CONTENT, infoRepository.findById(info.getId()).get().getContent());
    }

    //공지사항 수정 실패 테스트
    @Test
    @DisplayName("존재하지 않는 공지사항 수정")
    void editFail1() {
        //given
        Info info = infoRepository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());
        
        InfoEditRequest request = InfoEditRequest.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build();

        //when
        InfoNotFound e = assertThrows(InfoNotFound.class, () -> service.editInfo(1L, 2L, request.toDto()));

        //then
        assertEquals("존재하지 않는 공지사항입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
    }

    //공지사항 삭제 테스트
    @Test
    @DisplayName("공지사항 삭제")
    void delete() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("이창근")
                .nickname("불꽃남자")
                .role(Role.LEADER)
                .enrollmentNumber(19)
                .club(Club.SANTLE)
                .build());
        
        Info info = infoRepository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .member(member)
                .build());

        //when
        service.deleteInfo(member.getId(), info.getId());

        //then
        assertEquals(0, infoRepository.count());
    }

    //공지사항 삭제 실패 테스트
    @Test
    @DisplayName("존재하지 않는 공지사항 삭제")
    void deleteFail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("이창근")
                .nickname("불꽃남자")
                .role(Role.LEADER)
                .enrollmentNumber(19)
                .build());
        
        infoRepository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        //when
        InfoNotFound e = assertThrows(InfoNotFound.class, () -> service.deleteInfo(member.getId(), 2L));

        //then
        assertEquals("존재하지 않는 공지사항입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
    }
    
    //어드민 공지사항 수정 테스트
    @Test
    @DisplayName("어드민 공지사항 수정")
    void editByAdmin() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("이창근")
                .nickname("불꽃남자")
                .role(Role.LEADER)
                .enrollmentNumber(19)
                .club(Club.SANTLE)
                .build());
        
        Info info = infoRepository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .member(member)
                .build());
        
        InfoEditRequest request = InfoEditRequest.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build();
        
        //when
        service.editInfoByAdmin(info.getId(), request.toDto());
        
        //then
        assertEquals(NEW_TITLE, infoRepository.findById(info.getId()).get().getTitle());
        assertEquals(NEW_CONTENT, infoRepository.findById(info.getId()).get().getContent());
    }
    
    //어드민 공지사항 수정 실패 테스트
    @Test
    @DisplayName("어드민 존재하지 않는 공지사항 수정")
    void editByAdminFail1() {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("이창근")
                .nickname("불꽃남자")
                .role(Role.LEADER)
                .enrollmentNumber(19)
                .club(Club.SANTLE)
                .build());
        
        Info info = infoRepository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .member(member)
                .build());
        
        InfoEditRequest request = InfoEditRequest.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build();
        
        //when
        InfoNotFound e = assertThrows(InfoNotFound.class, () -> service.editInfoByAdmin(info.getId() + 1, request.toDto()));
        
        //then
        assertEquals("존재하지 않는 공지사항입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
    }
}