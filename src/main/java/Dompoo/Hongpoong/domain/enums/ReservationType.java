package Dompoo.Hongpoong.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationType {
	FIXED_TIME("정기연습"),
	NOT_FIXED_TIME("비정기연습"),
	;
	
	public final String korName;
}
