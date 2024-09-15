package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.domain.enums.Attendance;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationParticipate {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private Attendance attendance;
	
	@ManyToOne @JoinColumn(name = "reservation_id")
	private Reservation reservation;
	
	@ManyToOne @JoinColumn(name = "member_id")
	private Member member;
	
	public static List<ReservationParticipate> of(Reservation reservation, List<Member> members) {
		return members.stream()
				.map(member -> ReservationParticipate.builder()
						.member(member)
						.reservation(reservation)
						.attendance(Attendance.NOT_YET_ATTEND)
						.build())
				.toList();
	}
	
	public static ReservationParticipate of(Reservation reservation, Member member) {
		return ReservationParticipate.builder()
				.reservation(reservation)
				.member(member)
				.attendance(Attendance.NOT_YET_ATTEND)
				.build();
	}
	
	public void editAttendance(Attendance attendance) {
		this.attendance = attendance;
	}
	
	public void editAttendance(Boolean isLate) {
		this.attendance = isLate ? Attendance.LATE : Attendance.ATTEND;
	}
}
