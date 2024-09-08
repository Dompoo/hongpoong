package Dompoo.Hongpoong.api.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
	private String token;
	
	@Builder
	private LoginResponse(String token) {
		this.token = token;
	}
}
