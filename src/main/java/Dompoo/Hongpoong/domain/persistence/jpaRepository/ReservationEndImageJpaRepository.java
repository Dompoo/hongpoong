package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.ReservationEndImageJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationEndImageJpaRepository extends JpaRepository<ReservationEndImageJpaEntity, Long> {
	@Query("SELECT rei FROM ReservationEndImageJpaEntity rei WHERE rei.reservationJpaEntity = :reservation")
	List<ReservationEndImageJpaEntity> findAllByReservation(
			@Param("reservation") ReservationJpaEntity reservation
	);
}
