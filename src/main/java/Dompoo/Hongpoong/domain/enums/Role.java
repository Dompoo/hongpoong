package Dompoo.Hongpoong.domain.enums;

import Dompoo.Hongpoong.common.exception.impl.RoleException;

public enum Role {
	ROLE_USER("패원", 1),
	ROLE_LEADER("패짱", 2),
	ROLE_ADMIN("홍풍의장", 3),
	;
	
	public final String korName;
	public final int accessLevel;
	
	Role(String korName, int accessLevel) {
		this.korName = korName;
		this.accessLevel = accessLevel;
	}
	
	public static Role from(String value) {
		for (Role role : Role.values()) {
			if (role.korName.equals(value)) {
				return role;
			}
		}
		throw new RoleException();
	}
}
