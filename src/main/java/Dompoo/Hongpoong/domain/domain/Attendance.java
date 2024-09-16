package Dompoo.Hongpoong.domain.domain;

import Dompoo.Hongpoong.domain.enums.AttendanceStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Attendance {
	
	private Long id;
	private AttendanceStatus attendanceStatus;
	private Reservation reservation;
	private Member member;
	
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
	
	public void editAttendance(AttendanceStatus attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}
	
	public void editAttendance(Boolean isLate) {
		this.attendanceStatus = isLate ? AttendanceStatus.LATE : AttendanceStatus.ATTEND;
	}
}
