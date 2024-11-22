package Dompoo.Hongpoong.api.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordResetRequest {
    
    @NotBlank(message = "JWT 토큰은 비어있을 수 없습니다.")
    @Schema(example = "asda=qw-easd=zx-1321czxcasdasd")
    private final String token;
    
    @NotBlank(message = "새 비밀번호는 비어있을 수 없습니다.")
    @Schema(example = "password1234")
    private final String newPassword;
}
