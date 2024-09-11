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
	
	public void editAttendance(Attendance attendance) {
		this.attendance = attendance;
	}
}
