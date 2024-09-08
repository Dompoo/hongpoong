package Dompoo.Hongpoong.api.dto.common.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class SettingEditRequest {

    private final Boolean push;
    
    public SettingEditDto toDto() {
        return SettingEditDto.builder()
                .push(push)
                .build();
    }
}
