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
	
	public static Club from(String clubname) {
		for (Club club : Club.values()){
			if (club.korName.equals(clubname)) return club;
		}
		return ETC;
	}
}
