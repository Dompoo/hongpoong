package Dompoo.Hongpoong.domain.jpaRepository;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.enums.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstrumentJpaRepository extends JpaRepository<Instrument, Long> {
	
	@Query("SELECT i FROM Instrument i WHERE i.club != :club")
	List<Instrument> findAllByClubNotEquals(@Param("club") Club club);
	
	@Query("SELECT i FROM Instrument i WHERE i.club = :club")
	List<Instrument> findAllByClubEquals(@Param("club") Club club);
}
