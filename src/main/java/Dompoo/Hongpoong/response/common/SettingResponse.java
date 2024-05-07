package Dompoo.Hongpoong.response.common;

import Dompoo.Hongpoong.domain.Setting;
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

    public SettingResponse(Setting setting) {
        this.id = setting.getId();
        this.push = setting.isPush();
    }
}
