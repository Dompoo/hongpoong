package Dompoo.Hongpoong.api.dto.info.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InfoEditDto {

    private final String title;
    private final String content;
}
