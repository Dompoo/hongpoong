package Dompoo.Hongpoong.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AttendanceStatus {
	NOT_YET_ATTEND("미출석"),
	ATTEND("출석"),
	LATE("지각"),
	NO_SHOW("결석"),
	;
	
	public final String korName;
}
