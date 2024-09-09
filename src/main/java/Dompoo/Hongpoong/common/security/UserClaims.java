package Dompoo.Hongpoong.common.security;

import Dompoo.Hongpoong.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserClaims {
	private Long id;
	private String email;
	private Role role;
	
	@Builder
	private UserClaims(Long id, String email, Role role) {
		this.id = id;
		this.email = email;
		this.role = role;
	}
}
