package Dompoo.Hongpoong.domain.enums;

public enum Club {
	HWARANG("화랑"),
	SANTLE("산틀"),
	AKBAN("악반"),
	DEULNEOK("들녘"),
	ETC("개인");
	
	public final String korName;
	
	Club(String korName) {
		this.korName = korName;
	}
	
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
