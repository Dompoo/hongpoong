package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.domain.Reservation;
import Dompoo.Hongpoong.domain.domain.ReservationEndImage;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    
    Reservation findByIdJoinFetchCreator(Long reservationId);
    
    List<Reservation> findAllByDate(LocalDate date);
    
    List<Reservation> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<ReservationEndImage> findAllEndImageByReservation(Reservation reservation);
}
