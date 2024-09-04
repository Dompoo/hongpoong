package Dompoo.Hongpoong.api.dto.request.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
	private String email;
	private String password;
	
	//TODO : Validation 추가 필요
	@Builder
	private LoginRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
