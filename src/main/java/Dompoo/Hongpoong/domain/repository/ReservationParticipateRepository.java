package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationParticipateRepository extends JpaRepository<ReservationParticipate, Long> {
	@Query("SELECT rp FROM ReservationParticipate rp JOIN FETCH rp.reservation WHERE rp.member.id = :memberId AND rp.reservation.date = :localDate")
	List<ReservationParticipate> findByMemberIdAndReservationDate(@Param("memberId") Long memberId, @Param("localDate") LocalDate localDate);
	@Query("SELECT rp FROM ReservationParticipate rp JOIN FETCH rp.member WHERE rp.reservation.id = :reservationId")
	List<ReservationParticipate> findAllByReservationIdJoinFetchMember(@Param("reservationId") Long reservationId);
	@Query("SELECT rp.member FROM ReservationParticipate rp JOIN rp.member WHERE rp.reservation = :reservation")
	List<Member> findAllMemberByReservation(@Param("reservation") Reservation reservation);
	void deleteAllByReservationAndMemberIn(Reservation reservation, List<Member> members);
	void deleteAllByReservation(Reservation reservation);
}
