package Dompoo.Hongpoong.domain.enums;

import lombok.Getter;

@Getter
public enum Role {
	ROLE_USER(1),
	ROLE_LEADER(2),
	ROLE_ADMIN(3),
	;
	
	private final int accessLevel;
	
	Role(int accessLevel) {
		this.accessLevel = accessLevel;
	}
}
