package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.reservation.Reservation;
import Dompoo.Hongpoong.domain.enums.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByDate(LocalDate date);
    List<Reservation> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    @Query("SELECT r from Reservation r WHERE r.creator.club = :club AND r.date = :date")
    List<Reservation> findAllByClubAndDate(@Param("club") Club club, @Param("date") LocalDate localDate);
}
