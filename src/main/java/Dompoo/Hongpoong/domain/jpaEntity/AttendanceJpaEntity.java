package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.Attendance;
import Dompoo.Hongpoong.domain.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AttendanceJpaEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private AttendanceStatus attendanceStatus;
	
	@ManyToOne @JoinColumn(name = "reservation_id")
	private ReservationJpaEntity reservation;
	
	@ManyToOne @JoinColumn(name = "member_id")
	private MemberJpaEntity member;
	
	public Attendance toDomain() {
		return Attendance.builder()
				.id(this.id)
				.attendanceStatus(this.attendanceStatus)
				.reservation(this.reservation.toDomain())
				.member(this.member.toDomain())
				.build();
	}
	
	public static AttendanceJpaEntity of(Attendance attendance) {
		return AttendanceJpaEntity.builder()
				.id(attendance.getId())
				.attendanceStatus(attendance.getAttendanceStatus())
				.reservation(ReservationJpaEntity.of(attendance.getReservation()))
				.member(MemberJpaEntity.of(attendance.getMember()))
				.build();
	}
}
