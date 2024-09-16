package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationEndImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationEndImageJpaRepository extends JpaRepository<ReservationEndImage, Long> {
	List<ReservationEndImage> findAllByReservation(Reservation reservation);
}
