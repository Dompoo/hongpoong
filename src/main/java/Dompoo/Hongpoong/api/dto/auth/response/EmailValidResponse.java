package Dompoo.Hongpoong.api.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailValidResponse {
    
    @Schema(example = "true")
    private final Boolean valid;
}
