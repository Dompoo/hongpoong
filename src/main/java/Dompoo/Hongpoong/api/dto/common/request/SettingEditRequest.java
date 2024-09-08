package Dompoo.Hongpoong.api.dto.common.request;

import Dompoo.Hongpoong.api.dto.common.SettingSaveDto;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class SettingEditRequest {

    private final Boolean push;
    
    public SettingSaveDto toDto() {
        return SettingSaveDto.builder()
                .push(push)
                .build();
    }
}
