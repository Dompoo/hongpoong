package Dompoo.Hongpoong.common.security;

import Dompoo.Hongpoong.domain.enums.Role;

import java.util.List;

public enum SecurePolicy {
	MEMBER(List.of(
			Role.MEMBER,
			Role.PRIMARY_SOGO,
			Role.PRIMARY_BUK,
			Role.PRIMARY_JANGGU,
			Role.PRIMARY_JING,
			Role.PRIMARY_KKWANGGWARI,
			Role.LEADER,
			Role.ADMIN)
	),
	LEADER_PRIMARY(List.of(
			Role.PRIMARY_SOGO,
			Role.PRIMARY_BUK,
			Role.PRIMARY_JANGGU,
			Role.PRIMARY_JING,
			Role.PRIMARY_KKWANGGWARI,
			Role.LEADER,
			Role.ADMIN)
	),
	LEADER(List.of(
			Role.PRIMARY_KKWANGGWARI,
			Role.LEADER)
	),
	ADMIN_LEADER_PRIMARY(List.of(
			Role.PRIMARY_SOGO,
			Role.PRIMARY_BUK,
			Role.PRIMARY_JANGGU,
			Role.PRIMARY_JING,
			Role.PRIMARY_KKWANGGWARI,
			Role.LEADER,
			Role.ADMIN)
	),
	ADMIN_LEADER(List.of(
			Role.PRIMARY_KKWANGGWARI,
			Role.LEADER,
			Role.ADMIN)
	),
	ADMIN(List.of(
			Role.ADMIN)
	),
	;
	
	public final List<Role> acceptedRole;
	
	SecurePolicy(List<Role> acceptedRole) {
		this.acceptedRole = acceptedRole;
	}
}
