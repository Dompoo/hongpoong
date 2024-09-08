package Dompoo.Hongpoong.api.dto.info.request;

import Dompoo.Hongpoong.domain.entity.Info;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InfoCreateRequest {

    @NotBlank
    private final String title;
    @NotBlank
    private final String content;
    
    public Info toInfo(LocalDateTime now) {
        return Info.builder()
                .title(title)
                .content(content)
                .date(now)
                .build();
    }
}
