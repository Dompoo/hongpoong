package Dompoo.Hongpoong.domain.enums;

import Dompoo.Hongpoong.common.security.SecurePolicy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
	MEMBER("패원"),
	PRIMARY_JING("수징"),
	PRIMARY_JANGGU("상장구"),
	PRIMARY_BUK("수북"),
	PRIMARY_SOGO("수법고"),
	PRIMARY_KKWANGGWARI("상쇠"),
	LEADER("패짱"),
	ADMIN("홍풍의장"),
	;
	
	public final String korName;
	
	public boolean hasAccessLevelOf(SecurePolicy policy) {
		return policy.acceptedRole.contains(this);
	}
}
