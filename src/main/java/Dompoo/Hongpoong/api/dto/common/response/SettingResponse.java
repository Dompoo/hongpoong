package Dompoo.Hongpoong.api.dto.common.response;

import Dompoo.Hongpoong.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SettingResponse {
    
    @Schema(example = "10")
    private final Long memberId;
    
    @Schema(example = "true")
    private final Boolean pushAlarm;
    
    public static SettingResponse from(Member member) {
        return SettingResponse.builder()
                .memberId(member.getId())
                .pushAlarm(member.isPushAlarm())
                .build();
    }
}
