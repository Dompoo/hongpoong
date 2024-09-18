package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.domain.Reservation;
import Dompoo.Hongpoong.domain.domain.ReservationEndImage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    
    Optional<Reservation> findById(Long reservationId);
    
    Reservation findByIdJoinFetchCreator(Long reservationId);
    
    List<Reservation> findAllByDate(LocalDate date);
    
    List<Reservation> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<ReservationEndImage> findAllEndImageByReservation(Reservation reservation);
}
