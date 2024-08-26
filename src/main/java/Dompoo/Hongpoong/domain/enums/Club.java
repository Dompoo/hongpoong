package Dompoo.Hongpoong.domain.enums;

public enum Club {
	HWARANG, SANTLE, AKBAN, DEULNEOK, ETC;
	
	public static Club fromInt(int number) {
		return switch (number) {
			case 1 -> HWARANG;
			case 2 -> SANTLE;
			case 3 -> AKBAN;
			case 4 -> DEULNEOK;
			default -> ETC;
		};
	}
}
