package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.common.exception.impl.AttendanceNotFound;
import Dompoo.Hongpoong.domain.domain.Attendance;
import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.domain.Reservation;
import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.AttendanceJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {
	
	private final AttendanceJpaRepository attendanceJpaRepository;
	
	@Override
	public List<Attendance> findByMemberIdAndReservationDate(Long memberId, LocalDate localDate) {
		return attendanceJpaRepository.findByMemberIdAndReservationDate(memberId, localDate).stream()
				.map(AttendanceJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public List<Attendance> findAllByReservationIdJoinFetchMember(Long reservationId) {
		return attendanceJpaRepository.findAllByReservationIdJoinFetchMember(reservationId).stream()
				.map(AttendanceJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public List<Member> findAllMemberByReservation(Reservation reservation) {
		return attendanceJpaRepository.findAllMemberByReservation(ReservationJpaEntity.of(reservation)).stream()
				.map(MemberJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public Attendance findByMemberIdAndReservationId(Long memberId, Long reservationId) {
		return attendanceJpaRepository.findByMemberIdAndReservationId(memberId, reservationId)
				.orElseThrow(AttendanceNotFound::new)
				.toDomain();
	}
	
	@Override
	public List<Attendance> findByReservationIdAndNotAttend(Long reservationId) {
		return attendanceJpaRepository.findByReservationIdAndNotAttend(reservationId).stream()
				.map(AttendanceJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public List<Attendance> findAllByReservation(Reservation reservation) {
		return attendanceJpaRepository.findAllByReservation(ReservationJpaEntity.of(reservation)).stream()
				.map(AttendanceJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public void deleteAllByReservationAndMemberIn(Reservation reservation, List<Member> members) {
		attendanceJpaRepository.deleteAllByReservationAndMemberIn(ReservationJpaEntity.of(reservation), MemberJpaEntity.of(members));
	}
	
	@Override
	public void deleteAllByReservation(Reservation reservation) {
		attendanceJpaRepository.deleteAllByReservation(ReservationJpaEntity.of(reservation));
	}
}
