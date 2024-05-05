package Dompoo.Hongpoong.response;

import Dompoo.Hongpoong.domain.Setting;
import lombok.Getter;

@Getter
public class SettingResponse {

    private Long id;
    private boolean push;

    public SettingResponse(Setting setting) {
        this.id = setting.getId();
        this.push = setting.isPush();
    }
}
