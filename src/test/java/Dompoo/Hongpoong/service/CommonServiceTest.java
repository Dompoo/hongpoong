package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Setting;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.SettingRepository;
import Dompoo.Hongpoong.request.common.SettingSaveRequest;
import Dompoo.Hongpoong.response.SettingResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommonServiceTest {

    @Autowired
    private CommonService service;
    @Autowired
    private SettingRepository settingRepository;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void setUp() {
        memberRepository.deleteAll();
        settingRepository.deleteAll();
    }

    @Test
    @DisplayName("세팅 정보 불러오기")
    void get1() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("이창근")
                .password("1234")
                .club(Member.Club.SANTLE)
                .build());

        Setting setting = settingRepository.save(Setting.builder()
                .member(member)
                .build());

        //when
        SettingResponse response = service.getSetting(member.getId());

        //then
        assertEquals(setting.getId(), response.getId());
        assertFalse(response.isPush());
    }

    @Test
    @DisplayName("세팅 정보 저장하기")
    void save() {
        //given
        Member member = memberRepository.save(Member.builder()
                .email("dompoo@gmail.com")
                .username("이창근")
                .password("1234")
                .club(Member.Club.SANTLE)
                .build());

        Setting setting = settingRepository.save(Setting.builder()
                .member(member)
                .build());

        SettingSaveRequest request = SettingSaveRequest.builder()
                .push(true)
                .build();

        //when
        service.saveSetting(member.getId(), request);

        //then
        Setting findSetting = member.getSetting();
        assertTrue(findSetting.isPush());
    }
}