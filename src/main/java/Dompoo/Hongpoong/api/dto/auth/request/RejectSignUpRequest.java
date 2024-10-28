package Dompoo.Hongpoong.api.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class RejectSignUpRequest {
    
    @NotNull(message = "거절할 회원가입 요청은 필수입니다.")
    @Schema(example = "[1, 2, 3]")
    private final List<Long> rejectedSignUpIds;
}
