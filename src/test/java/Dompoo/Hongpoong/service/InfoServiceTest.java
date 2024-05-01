package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Info;
import Dompoo.Hongpoong.exception.InfoNotFound;
import Dompoo.Hongpoong.repository.InfoRepository;
import Dompoo.Hongpoong.request.info.InfoCreateRequest;
import Dompoo.Hongpoong.request.info.InfoEditRequest;
import Dompoo.Hongpoong.response.InfoDetailResponse;
import Dompoo.Hongpoong.response.InfoListResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class InfoServiceTest {


    @Autowired
    private InfoService service;

    @Autowired
    private InfoRepository repository;

    //info 서비스의 여러 메서드를 테스트하는 코드 작성

    private static final String TITLE = "공지사항 제목";
    private static final String CONTENT = "공지사항 내용";
    private static final String NEW_TITLE = "새로운 공지사항 제목";
    private static final String NEW_CONTENT = "새로운 공지사항 내용";

    @AfterEach
    void setUp() {
        repository.deleteAll();
    }

    //공지사항 추가 테스트
    @Test
    @DisplayName("공지사항 추가")
    void addOne() {
        //given
        InfoCreateRequest request = InfoCreateRequest.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();

        //when
        service.addInfo(request);

        //then
        assertEquals(1, repository.count());
        assertEquals(TITLE, repository.findAll().get(0).getTitle());
        assertEquals(CONTENT, repository.findAll().get(0).getContent());
    }

    //공지사항 전체 조회 테스트
    @Test
    @DisplayName("공지사항 전체 조회")
    void findAll() {
        //given
        repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        repository.save(Info.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build());

        //when
        List<InfoListResponse> list = service.getList();

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
        Info save = repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        //when
        InfoDetailResponse info = service.getDetail(save.getId());

        //then
        assertEquals(TITLE, info.getTitle());
        assertEquals(CONTENT, info.getContent());
    }

    //공지사항 상세 조회 실패 테스트
    @Test
    @DisplayName("존재하지 않는 공지사항 상세 조회")
    void findOneFail() {
        //given
        Info save = repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        //when
        InfoNotFound e = assertThrows(InfoNotFound.class, () -> service.getDetail(save.getId() + 1));

        //then
        assertEquals("존재하지 않는 공지사항입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
    }

    //공지사항 수정 테스트
    @Test
    @DisplayName("공지사항 수정")
    void edit() {
        //given
        Info save = repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        InfoEditRequest request = InfoEditRequest.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build();

        //when
        service.editInfo(save.getId(), request);

        //then
        assertEquals(NEW_TITLE, repository.findAll().get(0).getTitle());
        assertEquals(NEW_CONTENT, repository.findAll().get(0).getContent());
    }

    //공지사항 수정 실패 테스트
    @Test
    @DisplayName("존재하지 않는 공지사항 수정")
    void editFail1() {
        //given
        repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        InfoEditRequest request = InfoEditRequest.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build();

        //when
        InfoNotFound e = assertThrows(InfoNotFound.class, () -> service.editInfo(2L, request));

        //then
        assertEquals("존재하지 않는 공지사항입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
    }

    //공지사항 삭제 테스트
    @Test
    @DisplayName("공지사항 삭제")
    void delete() {
        //given
        Info info = repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        //when
        service.deleteInfo(info.getId());

        //then
        assertEquals(0, repository.count());
    }

    //공지사항 삭제 실패 테스트
    @Test
    @DisplayName("존재하지 않는 공지사항 삭제")
    void deleteFail() {
        //given
        repository.save(Info.builder()
                .title(TITLE)
                .content(CONTENT)
                .build());

        //when
        InfoNotFound e = assertThrows(InfoNotFound.class, () -> service.deleteInfo(2L));

        //then
        assertEquals("존재하지 않는 공지사항입니다.", e.getMessage());
        assertEquals("404", e.statusCode());
    }
}