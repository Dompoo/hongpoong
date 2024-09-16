package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationEndImage;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    
    Optional<Reservation> findByIdJoinFetchCreator(@Param("reservationId") Long reservationId);
    
    List<Reservation> findAllByDate(LocalDate date);
    
    List<Reservation> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<ReservationEndImage> findAllByReservation(Reservation reservation);
}
