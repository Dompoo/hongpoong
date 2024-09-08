package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.common.SettingResponse;
import Dompoo.Hongpoong.api.dto.common.SettingSaveRequest;
import Dompoo.Hongpoong.api.service.CommonService;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
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
    private MemberRepository memberRepository;

    @AfterEach
    void setUp() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("세팅 정보 불러오기")
    void get1() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .name("이창근")
                .password("1234")
                .club(Club.SANTLE)
                .build());

        //when
        SettingResponse response = service.getSetting(member.getId());

        //then
        assertEquals(member.getId(), response.getId());
        assertFalse(response.isPushAlarm());
    }

    @Test
    @DisplayName("세팅 정보 저장하기")
    void save() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .name("이창근")
                .password("1234")
                .club(Club.SANTLE)
                .build());

        SettingSaveRequest request = SettingSaveRequest.builder()
                .push(true)
                .build();

        //expected
        service.saveSetting(member.getId(), request.toDto());
    }
}