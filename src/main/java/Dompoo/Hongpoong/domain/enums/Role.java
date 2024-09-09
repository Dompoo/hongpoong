package Dompoo.Hongpoong.domain.enums;

import Dompoo.Hongpoong.common.exception.impl.RoleException;

public enum Role {
	USER("패원", 1),
	PRIMARY_JING("수징", 1),
	PRIMARY_JANGGU("상장구", 1),
	PRIMARY_BUK("수북", 1),
	PRIMARY_SOGO("수법고", 1),
	PRIMARY_KKWANGGWARI("상쇠", 2),
	LEADER("패짱", 2),
	ADMIN("홍풍의장", 3),
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
