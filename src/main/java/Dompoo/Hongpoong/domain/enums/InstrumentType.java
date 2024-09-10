package Dompoo.Hongpoong.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum InstrumentType {
	KKWANGGWARI("꽹과리"),
	JANGGU("장구"),
	BUK("북"),
	SOGO("소고"),
	JING("징"),
	ETC("기타 장비"),
	;
	
	public final String korName;
}
