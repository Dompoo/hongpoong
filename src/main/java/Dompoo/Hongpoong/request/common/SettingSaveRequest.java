package Dompoo.Hongpoong.request.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SettingSaveRequest {

    private boolean push;

    @Builder
    public SettingSaveRequest(boolean push) {
        this.push = push;
    }
}
