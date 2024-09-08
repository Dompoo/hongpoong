package Dompoo.Hongpoong.api.dto.info.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InfoEditRequest {
    
    @Schema(example = "걸궁 답사")
    private final String title;
    
    @Schema(example = "운영합니다.")
    private final String content;
    
    public InfoEditDto toDto() {
        return InfoEditDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}
