package Dompoo.Hongpoong.api.dto.request.common;

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
public class SettingSaveRequest {

    private Boolean push;

    @Builder
    private SettingSaveRequest(Boolean push) {
        this.push = push;
    }
    
    public SettingSaveDto toDto() {
        return SettingSaveDto.builder()
                .push(push)
                .build();
    }
}
