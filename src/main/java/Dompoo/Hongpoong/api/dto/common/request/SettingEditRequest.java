package Dompoo.Hongpoong.api.dto.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class SettingEditRequest {
    
    @Schema(example = "true")
    private final Boolean push;
    
    public SettingEditDto toDto() {
        return SettingEditDto.builder()
                .push(push)
                .build();
    }
}
