package Dompoo.Hongpoong.api.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class AcceptSignUpRequest {
    
    @NotNull(message = "승인/거절 여부는 필수입니다.")
    @Schema(example = "true")
    private final Boolean acceptResult;
}
