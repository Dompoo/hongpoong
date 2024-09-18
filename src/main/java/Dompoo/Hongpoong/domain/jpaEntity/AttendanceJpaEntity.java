package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.Attendance;
import Dompoo.Hongpoong.domain.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
	private ReservationJpaEntity reservationJpaEntity;
	
	@ManyToOne @JoinColumn(name = "member_id")
	private MemberJpaEntity memberJpaEntity;
	
	public Attendance toDomain() {
		return Attendance.builder()
				.id(this.id)
				.attendanceStatus(this.attendanceStatus)
				.reservation(this.reservationJpaEntity.toDomain())
				.member(this.memberJpaEntity.toDomain())
				.build();
	}
	
	public static AttendanceJpaEntity of(Attendance attendance) {
		return AttendanceJpaEntity.builder()
				.id(attendance.getId())
				.attendanceStatus(attendance.getAttendanceStatus())
				.reservationJpaEntity(ReservationJpaEntity.of(attendance.getReservation()))
				.memberJpaEntity(MemberJpaEntity.of(attendance.getMember()))
				.build();
	}
	
	public static List<AttendanceJpaEntity> of(List<Attendance> attendances) {
		return attendances.stream()
				.map(AttendanceJpaEntity::of)
				.toList();
	}
}
