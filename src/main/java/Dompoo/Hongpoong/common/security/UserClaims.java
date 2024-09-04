package Dompoo.Hongpoong.common.security;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserClaims {
	private Long id;
	private String email;
	
	@Builder
	private UserClaims(Long id, String email) {
		this.id = id;
		this.email = email;
	}
}
