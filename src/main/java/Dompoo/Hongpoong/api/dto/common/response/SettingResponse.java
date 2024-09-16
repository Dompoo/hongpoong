package Dompoo.Hongpoong.api.dto.common.response;

import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
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
    
    public static SettingResponse from(MemberJpaEntity memberJpaEntity) {
        return SettingResponse.builder()
                .memberId(memberJpaEntity.getId())
                .pushAlarm(memberJpaEntity.getPushAlarm())
                .build();
    }
}
