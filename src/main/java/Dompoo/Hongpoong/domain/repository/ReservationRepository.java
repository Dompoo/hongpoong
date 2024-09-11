package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    @Query("SELECT r FROM Reservation r JOIN FETCH r.creator WHERE r.id = :reservationId")
    Optional<Reservation> findByIdJoinFetchCreator(@Param("reservationId") Long reservationId);
    
    List<Reservation> findAllByDate(LocalDate date);
    List<Reservation> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
