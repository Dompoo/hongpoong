package Dompoo.Hongpoong.api.dto.common.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SettingEditDto {

    private final Boolean push;
}
