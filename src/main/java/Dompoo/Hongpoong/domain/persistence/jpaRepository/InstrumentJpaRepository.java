package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.InstrumentJpaEntity;
import Dompoo.Hongpoong.domain.enums.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstrumentJpaRepository extends JpaRepository<InstrumentJpaEntity, Long> {
	
	@Query("SELECT i FROM InstrumentJpaEntity i WHERE i.club != :club")
	List<InstrumentJpaEntity> findAllByClubNotEquals(@Param("club") Club club);
	
	@Query("SELECT i FROM InstrumentJpaEntity i WHERE i.club = :club")
	List<InstrumentJpaEntity> findAllByClubEquals(@Param("club") Club club);
}
