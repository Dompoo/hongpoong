package Dompoo.Hongpoong.api.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
/*
RequestBody
{
    "push": true
}
 */
public class SettingEditRequest {

    private Boolean push;

    @Builder
    private SettingEditRequest(Boolean push) {
        this.push = push;
    }
    
    public SettingSaveDto toDto() {
        return SettingSaveDto.builder()
                .push(push)
                .build();
    }
}
