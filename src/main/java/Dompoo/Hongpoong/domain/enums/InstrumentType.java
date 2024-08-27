package Dompoo.Hongpoong.domain.enums;

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
	
	public static InstrumentType fromInt(int number) {
		return switch (number) {
			case 1 -> KKWANGGWARI;
			case 2 -> JANGGU;
			case 3 -> BUK;
			case 4 -> SOGO;
			case 5 -> JING;
			default -> JANGGU;
		};
	}
}
