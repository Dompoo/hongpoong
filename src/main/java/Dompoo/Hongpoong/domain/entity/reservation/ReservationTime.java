package Dompoo.Hongpoong.domain.entity.reservation;

import Dompoo.Hongpoong.common.exception.impl.EndForwardStart;
import Dompoo.Hongpoong.common.exception.impl.ReservationTimeException;
import lombok.Getter;

@Getter
public enum ReservationTime {
	TIME_0900("0900"),
	TIME_0930("0930"),
	TIME_1000("1000"),
	TIME_1030("1030"),
	TIME_1100("1100"),
	TIME_1130("1130"),
	TIME_1200("1200"),
	TIME_1230("1230"),
	TIME_1300("1300"),
	TIME_1330("1330"),
	TIME_1400("1400"),
	TIME_1430("1430"),
	TIME_1500("1500"),
	TIME_1530("1530"),
	TIME_1600("1600"),
	TIME_1630("1630"),
	TIME_1700("1700"),
	TIME_1730("1730"),
	TIME_1800("1800"),
	TIME_1830("1830"),
	TIME_1900("1900"),
	TIME_1930("1930"),
	TIME_2000("2000"),
	TIME_2030("2030"),
	TIME_2100("2100"),
	TIME_2130("2130"),
	TIME_2200("2200"),
	TIME_2230("2230"),
	;
	
	private final String strValue;
	
	ReservationTime(String strValue) {
		this.strValue = strValue;
	}
	
	public static ReservationTime from(String strTime) {
		for (ReservationTime reservationTime : ReservationTime.values()) {
			if (strTime.equals(reservationTime.strValue)) {
				return reservationTime;
			}
		}
		throw new ReservationTimeException();
	}
	
	public static void validateStartTimeAndEndTime(String startTime, String endTime) {
		ReservationTime start = ReservationTime.from(startTime);
		ReservationTime end = ReservationTime.from(endTime);
		
		validateStartTimeAndEndTime(start, end);
	}
	
	public static void validateStartTimeAndEndTime(ReservationTime startTime, ReservationTime endTime) {
		if (startTime.ordinal() < endTime.ordinal()) {
			throw new EndForwardStart();
		}
	}
}
