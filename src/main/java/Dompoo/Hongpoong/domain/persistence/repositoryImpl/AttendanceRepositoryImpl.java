package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.domain.entity.Attendance;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.AttendanceJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {
	
	private final AttendanceJpaRepository attendanceJpaRepository;
	
	@Override
	public List<Attendance> findByMemberIdAndReservationDate(Long memberId, LocalDate localDate) {
		return attendanceJpaRepository.findByMemberIdAndReservationDate(memberId, localDate);
	}
	
	@Override
	public List<Attendance> findAllByReservationIdJoinFetchMember(Long reservationId) {
		return attendanceJpaRepository.findAllByReservationIdJoinFetchMember(reservationId);
	}
	
	@Override
	public List<Member> findAllMemberByReservation(Reservation reservation) {
		return attendanceJpaRepository.findAllMemberByReservation(reservation);
	}
	
	@Override
	public Optional<Attendance> findByMemberIdAndReservationId(Long memberId, Long reservationId) {
		return attendanceJpaRepository.findByMemberIdAndReservationId(memberId, reservationId);
	}
	
	@Override
	public List<Attendance> findByReservationIdAndNotAttend(Long reservationId) {
		return attendanceJpaRepository.findByReservationIdAndNotAttend(reservationId);
	}
	
	@Override
	public List<Attendance> findAllByReservation(Reservation reservation) {
		return attendanceJpaRepository.findAllByReservation(reservation);
	}
	
	@Override
	public void deleteAllByReservationAndMemberIn(Reservation reservation, List<Member> members) {
		attendanceJpaRepository.deleteAllByReservationAndMemberIn(reservation, members);
	}
	
	@Override
	public void deleteAllByReservation(Reservation reservation) {
		attendanceJpaRepository.deleteAllByReservation(reservation);
	}
}
