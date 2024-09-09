package Dompoo.Hongpoong.domain.enums;

import Dompoo.Hongpoong.common.exception.impl.RoleException;
import Dompoo.Hongpoong.common.security.SecurePolicy;

public enum Role {
	USER("패원", 1),
	PRIMARY_JING("수징", 2),
	PRIMARY_JANGGU("상장구", 2),
	PRIMARY_BUK("수북", 2),
	PRIMARY_SOGO("수법고", 2),
	PRIMARY_KKWANGGWARI("상쇠", 3),
	LEADER("패짱", 3),
	ADMIN("홍풍의장", 4),
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
	
	public boolean hasAccessLevelOf(SecurePolicy policy) {
		int accessLevel = this.accessLevel;
		
		return switch (policy) {
			case MEMBER -> accessLevel > 0;
			case LEADER -> accessLevel == 2;
			case LEADER_PRIMARY -> accessLevel == 2 || accessLevel == 3;
			case ADMIN_LEADER -> accessLevel > 2;
			case ADMIN_LEADER_PRIMARY -> accessLevel > 1;
			case ADMIN -> accessLevel > 3;
		};
	}
}
