package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationEndImageJpaEntity;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    
    Optional<ReservationJpaEntity> findByIdJoinFetchCreator(@Param("reservationId") Long reservationId);
    
    List<ReservationJpaEntity> findAllByDate(LocalDate date);
    
    List<ReservationJpaEntity> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<ReservationEndImageJpaEntity> findAllByReservation(ReservationJpaEntity reservationJpaEntity);
}
