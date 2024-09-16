package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.entity.Attendance;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {
	
	List<Attendance> findByMemberIdAndReservationDate(Long memberId, LocalDate localDate);
	
	List<Attendance> findAllByReservationIdJoinFetchMember(Long reservationId);
	
	List<Member> findAllMemberByReservation(Reservation reservation);
	
	Optional<Attendance> findByMemberIdAndReservationId(Long memberId, Long reservationId);
	
	List<Attendance> findByReservationIdAndNotAttend(Long reservationId);
	
	List<Attendance> findAllByReservation(Reservation reservation);
	
	void deleteAllByReservationAndMemberIn(Reservation reservation, List<Member> members);
	
	void deleteAllByReservation(Reservation reservation);
}
