package Dompoo.Hongpoong.api.dto.response.auth;

import lombok.Builder;

public class LoginResponse {
	private String token;
	
	@Builder
	private LoginResponse(String token) {
		this.token = token;
	}
}
