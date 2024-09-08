package Dompoo.Hongpoong.api.dto.info.request;

import Dompoo.Hongpoong.domain.entity.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InfoCreateRequest {

    @NotBlank
    @Schema(example = "걸궁 답사 공지")
    private final String title;
    
    @NotBlank
    @Schema(example = "안합니다.")
    private final String content;
    
    public Info toInfo(LocalDateTime now) {
        return Info.builder()
                .title(title)
                .content(content)
                .date(now)
                .build();
    }
}
