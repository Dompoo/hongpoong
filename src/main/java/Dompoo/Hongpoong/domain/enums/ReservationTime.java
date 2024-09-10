package Dompoo.Hongpoong.domain.enums;

import Dompoo.Hongpoong.common.exception.impl.EndForwardStart;
import Dompoo.Hongpoong.common.exception.impl.ReservationTimeException;

import java.time.LocalTime;

public enum ReservationTime {
	TIME_0900(LocalTime.of(9, 0)),
	TIME_0930(LocalTime.of(9, 30)),
	TIME_1000(LocalTime.of(10, 0)),
	TIME_1030(LocalTime.of(10, 30)),
	TIME_1100(LocalTime.of(11, 0)),
	TIME_1130(LocalTime.of(11, 30)),
	TIME_1200(LocalTime.of(12, 0)),
	TIME_1230(LocalTime.of(12, 30)),
	TIME_1300(LocalTime.of(13, 0)),
	TIME_1330(LocalTime.of(13, 30)),
	TIME_1400(LocalTime.of(14, 0)),
	TIME_1430(LocalTime.of(14, 30)),
	TIME_1500(LocalTime.of(15, 0)),
	TIME_1530(LocalTime.of(15, 30)),
	TIME_1600(LocalTime.of(16, 0)),
	TIME_1630(LocalTime.of(16, 30)),
	TIME_1700(LocalTime.of(17, 0)),
	TIME_1730(LocalTime.of(17, 30)),
	TIME_1800(LocalTime.of(18, 0)),
	TIME_1830(LocalTime.of(18, 30)),
	TIME_1900(LocalTime.of(19, 0)),
	TIME_1930(LocalTime.of(19, 30)),
	TIME_2000(LocalTime.of(20, 0)),
	TIME_2030(LocalTime.of(20, 30)),
	TIME_2100(LocalTime.of(21, 0)),
	TIME_2130(LocalTime.of(21, 30)),
	TIME_2200(LocalTime.of(22, 0)),
	TIME_2230(LocalTime.of(22, 30)),
	;
	
	public final LocalTime localTime;
	
	ReservationTime(LocalTime localTime) {
		this.localTime = localTime;
	}
	
	public static ReservationTime from(LocalTime value) {
		for (ReservationTime reservationTime : ReservationTime.values()) {
			if (value.equals(reservationTime.localTime)) {
				return reservationTime;
			}
		}
		throw new ReservationTimeException();
	}
	
	public static void validateStartTimeAndEndTime(ReservationTime startTime, ReservationTime endTime) {
		validateStartTimeAndEndTime(startTime.localTime, endTime.localTime);
	}
	
	public static void validateStartTimeAndEndTime(LocalTime startTime, LocalTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new EndForwardStart();
		}
	}
	
	public boolean isBetween(LocalTime startTime, LocalTime endTime) {
		return startTime.isBefore(this.localTime) && this.localTime.isBefore(endTime);
	}
	
	public ReservationTime nextReservationTime() {
		return from(this.localTime.plusMinutes(30));
	}
}
