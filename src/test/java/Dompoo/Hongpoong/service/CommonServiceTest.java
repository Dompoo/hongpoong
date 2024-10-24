package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.common.request.SettingEditRequest;
import Dompoo.Hongpoong.api.dto.common.response.SettingResponse;
import Dompoo.Hongpoong.api.service.CommonService;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
class CommonServiceTest {

    @Autowired
    private CommonService service;
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @AfterEach
    void setUp() {
        memberJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("세팅 정보 불러오기")
    void get1() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .email("dompoo@gmail.com")
                .name("이창근")
                .password("1234")
                .club(Club.SANTLE)
                .pushAlarm(false)
                .build());

        //when
        SettingResponse response = service.findMySetting(memberJpaEntity.getId());

        //then
        assertEquals(memberJpaEntity.getId(), response.getMemberId());
        assertFalse(response.getPushAlarm());
    }

    @Test
    @DisplayName("세팅 정보 저장하기")
    void save() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .email("dompoo@gmail.com")
                .name("이창근")
                .password("1234")
                .club(Club.SANTLE)
                .build());

        SettingEditRequest request = SettingEditRequest.builder()
                .push(true)
                .build();

        //expected
        service.editSetting(memberJpaEntity.getId(), request.toDto());
    }
}