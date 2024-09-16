package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationEndImageJpaEntity;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ReservationEndImageJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ReservationJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {
	
	private final ReservationJpaRepository reservationJpaRepository;
	private final ReservationEndImageJpaRepository reservationEndImageJpaRepository;
	
	@Override
	public Optional<ReservationJpaEntity> findByIdJoinFetchCreator(Long reservationId) {
		return reservationJpaRepository.findByIdJoinFetchCreator(reservationId);
	}
	
	@Override
	public List<ReservationJpaEntity> findAllByDate(LocalDate date) {
		return reservationJpaRepository.findAllByDate(date);
	}
	
	@Override
	public List<ReservationJpaEntity> findAllByDateBetween(LocalDate startDate, LocalDate endDate) {
		return reservationJpaRepository.findAllByDateBetween(startDate, endDate);
	}
	
	@Override
	public List<ReservationEndImageJpaEntity> findAllByReservation(ReservationJpaEntity reservationJpaEntity) {
		return reservationEndImageJpaRepository.findAllByReservation(reservationJpaEntity);
	}
}
