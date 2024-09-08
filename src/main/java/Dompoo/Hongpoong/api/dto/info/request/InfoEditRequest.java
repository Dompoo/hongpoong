package Dompoo.Hongpoong.api.dto.info.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class InfoEditRequest {

    private final String title;
    private final String content;
    
    public InfoEditDto toDto() {
        return InfoEditDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}
