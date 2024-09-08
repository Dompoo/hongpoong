package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.common.SettingResponse;
import Dompoo.Hongpoong.api.dto.common.SettingSaveRequest;
import Dompoo.Hongpoong.config.MyWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommonControllerTest extends MyWebMvcTest {
    
    private static final Long SETTING_ID = 1L;
    private static final boolean PUSH_ALARM = false;

    @Test
    @DisplayName("세팅 정보 불러오기")
    void settingList() throws Exception {
        //given
        when(commonService.getSetting(any())).thenReturn(
                SettingResponse.builder()
                        .id(SETTING_ID)
                        .pushAlarm(PUSH_ALARM)
                        .build()
        );

        //expected
        mockMvc.perform(get("/common/setting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SETTING_ID))
                .andExpect(jsonPath("$.pushAlarm").value(PUSH_ALARM))
                .andDo(print());
    }

    @Test
    @DisplayName("세팅 하기")
    void doSetting() throws Exception {
        //given
        SettingSaveRequest request = SettingSaveRequest.builder()
                .push(false)
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