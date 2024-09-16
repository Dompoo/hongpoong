package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationEndImage;
import Dompoo.Hongpoong.domain.jpaRepository.ReservationEndImageJpaRepository;
import Dompoo.Hongpoong.domain.jpaRepository.ReservationJpaRepository;
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
	public Optional<Reservation> findByIdJoinFetchCreator(Long reservationId) {
		return reservationJpaRepository.findByIdJoinFetchCreator(reservationId);
	}
	
	@Override
	public List<Reservation> findAllByDate(LocalDate date) {
		return reservationJpaRepository.findAllByDate(date);
	}
	
	@Override
	public List<Reservation> findAllByDateBetween(LocalDate startDate, LocalDate endDate) {
		return reservationJpaRepository.findAllByDateBetween(startDate, endDate);
	}
	
	@Override
	public List<ReservationEndImage> findAllByReservation(Reservation reservation) {
		return reservationEndImageJpaRepository.findAllByReservation(reservation);
	}
}
