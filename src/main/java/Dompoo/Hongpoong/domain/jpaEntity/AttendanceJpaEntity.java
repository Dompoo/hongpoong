package Dompoo.Hongpoong.domain.jpaEntity;

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
	
	public static List<AttendanceJpaEntity> of(ReservationJpaEntity reservationJpaEntity, List<MemberJpaEntity> memberJpaEntities) {
		return memberJpaEntities.stream()
				.map(member -> AttendanceJpaEntity.builder()
						.memberJpaEntity(member)
						.reservationJpaEntity(reservationJpaEntity)
						.attendanceStatus(AttendanceStatus.NOT_YET_ATTEND)
						.build())
				.toList();
	}
	
	public static AttendanceJpaEntity of(ReservationJpaEntity reservationJpaEntity, MemberJpaEntity memberJpaEntity) {
		return AttendanceJpaEntity.builder()
				.reservationJpaEntity(reservationJpaEntity)
				.memberJpaEntity(memberJpaEntity)
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
