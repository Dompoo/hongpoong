package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceJpaRepository extends JpaRepository<AttendanceJpaEntity, Long> {
	
	@Query("SELECT a FROM AttendanceJpaEntity a JOIN FETCH a.reservationJpaEntity WHERE a.memberJpaEntity.id = :memberId AND a.reservationJpaEntity.date = :localDate")
	List<AttendanceJpaEntity> findByMemberIdAndReservationDate(
			@Param("memberId") Long memberId,
			@Param("localDate") LocalDate localDate
	);
	
	@Query("SELECT rp FROM AttendanceJpaEntity rp JOIN FETCH rp.memberJpaEntity WHERE rp.memberJpaEntity.id = :reservationId")
	List<AttendanceJpaEntity> findAllByReservationIdJoinFetchMember(
			@Param("reservationId") Long reservationId
	);
	
	@Query("SELECT rp.memberJpaEntity FROM AttendanceJpaEntity rp JOIN rp.memberJpaEntity WHERE rp.memberJpaEntity = :reservation")
	List<MemberJpaEntity> findAllMemberByReservation(
			@Param("reservation") ReservationJpaEntity reservation
	);
	
	@Query("SELECT rp FROM AttendanceJpaEntity rp JOIN FETCH rp.memberJpaEntity JOIN FETCH rp.memberJpaEntity WHERE rp.memberJpaEntity.id = :memberId AND rp.memberJpaEntity.id = :reservationId")
	Optional<AttendanceJpaEntity> findByMemberIdAndReservationId(
			@Param("memberId") Long memberId,
			@Param("reservationId") Long reservationId
	);
	
	@Query("SELECT rp FROM AttendanceJpaEntity rp WHERE rp.memberJpaEntity.id = :reservationId AND rp.attendanceStatus = 'NOT_YET_ATTEND'")
	List<AttendanceJpaEntity> findByReservationIdAndNotAttend(
			@Param("reservationId") Long reservationId
	);
	
	@Query("SELECT rp FROM AttendanceJpaEntity rp JOIN FETCH rp.memberJpaEntity JOIN FETCH rp.memberJpaEntity WHERE rp.memberJpaEntity.id = :reservation")
	List<AttendanceJpaEntity> findAllByReservation(
			@Param("reservation") ReservationJpaEntity reservation
	);
	
	@Modifying
	@Query("DELETE FROM AttendanceJpaEntity rp WHERE rp.reservationJpaEntity = :reservation AND rp.memberJpaEntity IN :members")
	void deleteAllByReservationAndMemberIn(
			@Param("reservation") ReservationJpaEntity reservation,
			@Param("members") List<MemberJpaEntity> members
	);
	
	@Modifying
	@Query("DELETE FROM AttendanceJpaEntity rp WHERE rp.memberJpaEntity.id = :memberId AND rp.memberJpaEntity.id = :reservationId")
	void deleteAllByReservation(
			@Param("reservation") ReservationJpaEntity reservation
	);
}
