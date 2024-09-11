package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationEndImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationEndImageRepository extends JpaRepository<ReservationEndImage, Long> {
	List<ReservationEndImage> findAllByReservation(Reservation reservation);
}
