package Dompoo.Hongpoong.api.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class EmailValidRequest {
    
    @NotBlank(message = "이메일을 입력해주세요.")
    private final String email;
}

