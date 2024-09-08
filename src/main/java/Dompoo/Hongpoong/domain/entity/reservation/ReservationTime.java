package Dompoo.Hongpoong.domain.entity.reservation;

import Dompoo.Hongpoong.common.exception.impl.EndForwardStart;
import Dompoo.Hongpoong.common.exception.impl.ReservationTimeException;

import java.time.LocalTime;

public enum ReservationTime {
	TIME_0900("0900", LocalTime.of(9, 0)),
	TIME_0930("0930", LocalTime.of(9, 30)),
	TIME_1000("1000", LocalTime.of(10, 0)),
	TIME_1030("1030", LocalTime.of(10, 30)),
	TIME_1100("1100", LocalTime.of(11, 0)),
	TIME_1130("1130", LocalTime.of(11, 30)),
	TIME_1200("1200", LocalTime.of(12, 0)),
	TIME_1230("1230", LocalTime.of(12, 30)),
	TIME_1300("1300", LocalTime.of(13, 0)),
	TIME_1330("1330", LocalTime.of(13, 30)),
	TIME_1400("1400", LocalTime.of(14, 0)),
	TIME_1430("1430", LocalTime.of(14, 30)),
	TIME_1500("1500", LocalTime.of(15, 0)),
	TIME_1530("1530", LocalTime.of(15, 30)),
	TIME_1600("1600", LocalTime.of(16, 0)),
	TIME_1630("1630", LocalTime.of(16, 30)),
	TIME_1700("1700", LocalTime.of(17, 0)),
	TIME_1730("1730", LocalTime.of(17, 30)),
	TIME_1800("1800", LocalTime.of(18, 0)),
	TIME_1830("1830", LocalTime.of(18, 30)),
	TIME_1900("1900", LocalTime.of(19, 0)),
	TIME_1930("1930", LocalTime.of(19, 30)),
	TIME_2000("2000", LocalTime.of(20, 0)),
	TIME_2030("2030", LocalTime.of(20, 30)),
	TIME_2100("2100", LocalTime.of(21, 0)),
	TIME_2130("2130", LocalTime.of(21, 30)),
	TIME_2200("2200", LocalTime.of(22, 0)),
	TIME_2230("2230", LocalTime.of(22, 30)),
	;
	
	public final String strValue;
	public final LocalTime localTime;
	
	ReservationTime(String strValue, LocalTime localTime) {
		this.strValue = strValue;
		this.localTime = localTime;
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
		if (startTime.ordinal() > endTime.ordinal()) {
			throw new EndForwardStart();
		}
	}
}
