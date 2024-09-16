package Dompoo.Hongpoong.domain.domain;

import Dompoo.Hongpoong.domain.enums.AttendanceStatus;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Attendance {
	
	private final Long id;
	private final AttendanceStatus attendanceStatus;
	private final Reservation reservation;
	private final Member member;
	
	public static List<Attendance> of(Reservation reservation, List<Member> members) {
		return members.stream()
				.map(member -> Attendance.builder()
						.member(member)
						.reservation(reservation)
						.attendanceStatus(AttendanceStatus.NOT_YET_ATTEND)
						.build())
				.toList();
	}
	
	public static Attendance of(Reservation reservation, Member member) {
		return Attendance.builder()
				.reservation(reservation)
				.member(member)
				.attendanceStatus(AttendanceStatus.NOT_YET_ATTEND)
				.build();
	}
	
	public Attendance withAttendance(AttendanceStatus attendanceStatus) {
		return Attendance.builder()
				.id(this.id)
				.attendanceStatus(attendanceStatus)
				.reservation(this.reservation)
				.member(this.member)
				.build();
	}
	
	public Attendance withAttendance(ReservationTime startTime, LocalDateTime now) {
		return Attendance.builder()
				.id(this.id)
				.attendanceStatus(startTime.localTime.isBefore(now.toLocalTime()) ? AttendanceStatus.LATE : AttendanceStatus.ATTEND)
				.reservation(this.reservation)
				.member(this.member)
				.build();
	}
}
