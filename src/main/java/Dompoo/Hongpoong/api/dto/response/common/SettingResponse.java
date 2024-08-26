package Dompoo.Hongpoong.api.dto.response.common;

import Dompoo.Hongpoong.domain.Setting;
import lombok.Builder;
import lombok.Getter;

@Getter
/*
ResponseBody

<단건조회시>
{
    "push": false
}
 */
public class SettingResponse {

    private Long id;
    private boolean push;
    
    @Builder
    private SettingResponse(Long id, boolean push) {
        this.id = id;
        this.push = push;
    }
    
    public static SettingResponse from(Setting setting) {
        return SettingResponse.builder()
                .id(setting.getId())
                .push(setting.isPush())
                .build();
    }
}
