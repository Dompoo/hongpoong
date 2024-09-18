package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationJpaEntity, Long> {
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE r.id = :reservationId")
    Optional<ReservationJpaEntity> findByIdJoinFetchCreator(
            @Param("reservationId") Long reservationId
    );
    
    List<ReservationJpaEntity> findAllByDate(LocalDate date);
    
    List<ReservationJpaEntity> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
