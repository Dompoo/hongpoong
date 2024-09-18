package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.common.exception.impl.ReservationNotFound;
import Dompoo.Hongpoong.domain.domain.Reservation;
import Dompoo.Hongpoong.domain.domain.ReservationEndImage;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationEndImageJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
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
	public Reservation save(Reservation reservation) {
		return reservationJpaRepository.save(ReservationJpaEntity.of(reservation))
				.toDomain();
	}
	
	@Override
	public void saveAllReservationEndImage(List<ReservationEndImage> reservationEndImages) {
		reservationEndImageJpaRepository.saveAll(reservationEndImages.stream()
				.map(ReservationEndImageJpaEntity::of)
				.toList());
	}
	
	@Override
	public Optional<Reservation> findById(Long reservationId) {
		return reservationJpaRepository.findById(reservationId)
				.map(ReservationJpaEntity::toDomain);
	}
	
	@Override
	public Reservation findByIdJoinFetchCreator(Long reservationId) {
		return reservationJpaRepository.findByIdJoinFetchCreator(reservationId)
				.orElseThrow(ReservationNotFound::new)
				.toDomain();
	}
	
	@Override
	public List<Reservation> findAllByDate(LocalDate date) {
		return reservationJpaRepository.findAllByDate(date).stream()
				.map(ReservationJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public List<Reservation> findAllByDateBetween(LocalDate startDate, LocalDate endDate) {
		return reservationJpaRepository.findAllByDateBetween(startDate, endDate).stream()
				.map(ReservationJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public List<ReservationEndImage> findAllEndImageByReservation(Reservation reservation) {
		return reservationEndImageJpaRepository.findAllByReservation(ReservationJpaEntity.of(reservation)).stream()
				.map(ReservationEndImageJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public void delete(Reservation reservation) {
		reservationJpaRepository.delete(ReservationJpaEntity.of(reservation));
	}
}
