package Dompoo.Hongpoong.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationType {
	REGULAR("정규연습"),
	COMMON("일반연습"),
	;
	
	public final String korName;
}
