package Dompoo.Hongpoong.api.dto.common.response;

import Dompoo.Hongpoong.domain.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SettingResponse {

    private final Long id;
    private final Boolean pushAlarm;
    
    public static SettingResponse from(Member member) {
        return SettingResponse.builder()
                .id(member.getId())
                .pushAlarm(member.isPushAlarm())
                .build();
    }
}
