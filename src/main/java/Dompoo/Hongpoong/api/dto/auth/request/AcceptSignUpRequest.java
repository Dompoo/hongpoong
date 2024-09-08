package Dompoo.Hongpoong.api.dto.auth.request;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class AcceptSignUpRequest {

    @Min(value = 1, message = "승인/거절할 회원가입 요청을 선택해주세요.")
    private final Long signupId;

    private final Boolean acceptResult;
}
