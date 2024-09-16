package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.domain.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Attendance {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private AttendanceStatus attendanceStatus;
	
	@ManyToOne @JoinColumn(name = "reservation_id")
	private Reservation reservation;
	
	@ManyToOne @JoinColumn(name = "member_id")
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
