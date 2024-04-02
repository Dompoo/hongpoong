package Dompoo.Hongpoong.repository;

import Dompoo.Hongpoong.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByDate(LocalDate date);
}
