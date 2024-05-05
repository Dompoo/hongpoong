package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.WithMockMember;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Setting;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.SettingRepository;
import Dompoo.Hongpoong.request.common.SettingSaveRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CommonControllerTest {

    @Autowired
    private SettingRepository settingRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void setUp() {
        memberRepository.deleteAll();
        settingRepository.deleteAll();
    }

    @Test
    @DisplayName("세팅 정보 불러오기")
    @WithMockMember
    void settingList() throws Exception {
        //given
        Member member = memberRepository.findAll().getFirst();

        Setting setting = settingRepository.save(Setting.builder()
                .member(member)
                .build());

        //expected
        mockMvc.perform(get("/common/setting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(setting.getId()))
                .andExpect(jsonPath("$.push").value(false))
                .andDo(print());
    }

    @Test
    @DisplayName("세팅 하기")
    @WithMockMember
    void doSetting() throws Exception {
        //given
        Member member = memberRepository.findAll().getFirst();

        Setting setting = settingRepository.save(Setting.builder()
                .member(member)
                .build());

        SettingSaveRequest request = SettingSaveRequest.builder()
                .push(true)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/common/setting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

}