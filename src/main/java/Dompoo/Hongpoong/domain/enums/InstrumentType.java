package Dompoo.Hongpoong.domain.enums;

public enum InstrumentType {
	KKWANGGWARI, JANGGU, BUK, SOGO, JING;
	
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
