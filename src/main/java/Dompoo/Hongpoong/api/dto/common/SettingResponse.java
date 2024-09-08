package Dompoo.Hongpoong.api.dto.common;

import Dompoo.Hongpoong.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
/*
ResponseBody

<단건조회시>
{
    "pushAlarm": false
}
 */
public class SettingResponse {

    private Long id;
    private boolean pushAlarm;
    
    @Builder
    private SettingResponse(Long id, boolean pushAlarm) {
        this.id = id;
        this.pushAlarm = pushAlarm;
    }
    
    public static SettingResponse from(Member member) {
        return SettingResponse.builder()
                .id(member.getId())
                .pushAlarm(member.isPushAlarm())
                .build();
    }
}
