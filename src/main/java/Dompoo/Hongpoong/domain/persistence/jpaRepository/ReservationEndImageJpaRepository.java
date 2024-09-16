package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationEndImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationEndImageJpaRepository extends JpaRepository<ReservationEndImageJpaEntity, Long> {
	List<ReservationEndImageJpaEntity> findAllByReservation(ReservationJpaEntity reservationJpaEntity);
}
