package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceJpaRepository extends JpaRepository<AttendanceJpaEntity, Long> {
	
	@Query("SELECT rp FROM AttendanceJpaEntity rp JOIN FETCH rp.reservation WHERE rp.member.id = :memberId AND rp.reservation.date = :localDate")
	List<AttendanceJpaEntity> findByMemberIdAndReservationDate(@Param("memberId") Long memberId, @Param("localDate") LocalDate localDate);
	
	@Query("SELECT rp FROM AttendanceJpaEntity rp JOIN FETCH rp.member WHERE rp.reservation.id = :reservationId")
	List<AttendanceJpaEntity> findAllByReservationIdJoinFetchMember(@Param("reservationId") Long reservationId);
	
	@Query("SELECT rp.member FROM AttendanceJpaEntity rp JOIN rp.member WHERE rp.reservation = :reservation")
	List<MemberJpaEntity> findAllMemberByReservation(@Param("reservation") ReservationJpaEntity reservationJpaEntity);
	
	@Query("SELECT rp FROM AttendanceJpaEntity rp JOIN FETCH rp.reservation JOIN FETCH rp.member WHERE rp.member.id = :memberId AND rp.reservation.id = :reservationId")
	Optional<AttendanceJpaEntity> findByMemberIdAndReservationId(@Param("memberId") Long memberId, @Param("reservationId") Long reservationId);
	
	@Query("SELECT rp FROM AttendanceJpaEntity rp WHERE rp.reservation.id = :reservationId AND rp.attendanceStatus = 'NOT_YET_ATTEND'")
	List<AttendanceJpaEntity> findByReservationIdAndNotAttend(@Param("reservationId") Long reservationId);
	
	List<AttendanceJpaEntity> findAllByReservation(ReservationJpaEntity reservationJpaEntity);
	
	void deleteAllByReservationAndMemberIn(ReservationJpaEntity reservationJpaEntity, List<MemberJpaEntity> memberJpaEntities);
	
	void deleteAllByReservation(ReservationJpaEntity reservationJpaEntity);
}
