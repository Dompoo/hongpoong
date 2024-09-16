package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Attendance;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
	@Query("SELECT rp FROM Attendance rp JOIN FETCH rp.reservation WHERE rp.member.id = :memberId AND rp.reservation.date = :localDate")
	List<Attendance> findByMemberIdAndReservationDate(@Param("memberId") Long memberId, @Param("localDate") LocalDate localDate);
	
	@Query("SELECT rp FROM Attendance rp JOIN FETCH rp.member WHERE rp.reservation.id = :reservationId")
	List<Attendance> findAllByReservationIdJoinFetchMember(@Param("reservationId") Long reservationId);
	
	@Query("SELECT rp.member FROM Attendance rp JOIN rp.member WHERE rp.reservation = :reservation")
	List<Member> findAllMemberByReservation(@Param("reservation") Reservation reservation);
	
	@Query("SELECT rp FROM Attendance rp JOIN FETCH rp.reservation JOIN FETCH rp.member WHERE rp.member.id = :memberId AND rp.reservation.id = :reservationId")
	Optional<Attendance> findByMemberIdAndReservationId(@Param("memberId") Long memberId, @Param("reservationId") Long reservationId);
	
	@Query("SELECT rp FROM Attendance rp WHERE rp.reservation.id = :reservationId AND rp.attendanceStatus = 'NOT_YET_ATTEND'")
	List<Attendance> findByReservationIdAndNotAttend(@Param("reservationId") Long reservationId);
	
	List<Attendance> findAllByReservation(Reservation reservation);
	
	void deleteAllByReservationAndMemberIn(Reservation reservation, List<Member> members);
	
	void deleteAllByReservation(Reservation reservation);
}
