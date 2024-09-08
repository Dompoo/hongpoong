package Dompoo.Hongpoong.api.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter
/*
RequestBody
{
    "push": true
}
 */
public class SettingSaveDto {

    private Boolean push;

    @Builder
    private SettingSaveDto(Boolean push) {
        this.push = push;
    }
}
