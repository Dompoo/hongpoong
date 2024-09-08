package Dompoo.Hongpoong.domain.enums;

import Dompoo.Hongpoong.common.exception.impl.InstrumentTypeException;

public enum InstrumentType {
	KKWANGGWARI("꽹과리"),
	JANGGU("장구"),
	BUK("북"),
	SOGO("소고"),
	JING("징"),
	;
	
	public final String korName;
	
	InstrumentType(String korName) {
		this.korName = korName;
	}
	
	public static InstrumentType from(String strInstrument) {
		for (InstrumentType instrumentType : InstrumentType.values()) {
			if (instrumentType.korName.equals(strInstrument)) {
				return instrumentType;
			}
		}
		throw new InstrumentTypeException();
	}
}
