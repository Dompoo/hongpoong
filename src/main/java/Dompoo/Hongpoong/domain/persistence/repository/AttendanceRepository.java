package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {
	
	List<AttendanceJpaEntity> findByMemberIdAndReservationDate(Long memberId, LocalDate localDate);
	
	List<AttendanceJpaEntity> findAllByReservationIdJoinFetchMember(Long reservationId);
	
	List<MemberJpaEntity> findAllMemberByReservation(ReservationJpaEntity reservationJpaEntity);
	
	Optional<AttendanceJpaEntity> findByMemberIdAndReservationId(Long memberId, Long reservationId);
	
	List<AttendanceJpaEntity> findByReservationIdAndNotAttend(Long reservationId);
	
	List<AttendanceJpaEntity> findAllByReservation(ReservationJpaEntity reservationJpaEntity);
	
	void deleteAllByReservationAndMemberIn(ReservationJpaEntity reservationJpaEntity, List<MemberJpaEntity> memberJpaEntities);
	
	void deleteAllByReservation(ReservationJpaEntity reservationJpaEntity);
}
