package Dompoo.Hongpoong.api.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class LoginRequest {
	
	@NotBlank(message = "이메일을 입력해주세요.")
	private final String email;
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private final String password;
}
