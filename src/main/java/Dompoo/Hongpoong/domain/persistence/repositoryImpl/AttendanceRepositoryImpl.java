package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
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
	public List<AttendanceJpaEntity> findByMemberIdAndReservationDate(Long memberId, LocalDate localDate) {
		return attendanceJpaRepository.findByMemberIdAndReservationDate(memberId, localDate);
	}
	
	@Override
	public List<AttendanceJpaEntity> findAllByReservationIdJoinFetchMember(Long reservationId) {
		return attendanceJpaRepository.findAllByReservationIdJoinFetchMember(reservationId);
	}
	
	@Override
	public List<MemberJpaEntity> findAllMemberByReservation(ReservationJpaEntity reservationJpaEntity) {
		return attendanceJpaRepository.findAllMemberByReservation(reservationJpaEntity);
	}
	
	@Override
	public Optional<AttendanceJpaEntity> findByMemberIdAndReservationId(Long memberId, Long reservationId) {
		return attendanceJpaRepository.findByMemberIdAndReservationId(memberId, reservationId);
	}
	
	@Override
	public List<AttendanceJpaEntity> findByReservationIdAndNotAttend(Long reservationId) {
		return attendanceJpaRepository.findByReservationIdAndNotAttend(reservationId);
	}
	
	@Override
	public List<AttendanceJpaEntity> findAllByReservation(ReservationJpaEntity reservationJpaEntity) {
		return attendanceJpaRepository.findAllByReservation(reservationJpaEntity);
	}
	
	@Override
	public void deleteAllByReservationAndMemberIn(ReservationJpaEntity reservationJpaEntity, List<MemberJpaEntity> memberJpaEntities) {
		attendanceJpaRepository.deleteAllByReservationAndMemberIn(reservationJpaEntity, memberJpaEntities);
	}
	
	@Override
	public void deleteAllByReservation(ReservationJpaEntity reservationJpaEntity) {
		attendanceJpaRepository.deleteAllByReservation(reservationJpaEntity);
	}
}
