package Dompoo.Hongpoong.common.security;

import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserClaims {
	private Long id;
	private String email;
	private Role role;
	private Club club;
	
	@Builder
	private UserClaims(Long id, String email, Role role, Club club) {
		this.id = id;
		this.email = email;
		this.role = role;
		this.club = club;
	}
}
