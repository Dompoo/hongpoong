package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.enums.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
	
	@Query("select i from Instrument i where i.borrower.club != :club")
	List<Instrument> findAllByClubNotEquals(@Param("club") Club club);
	
	@Query("select i from Instrument i where i.borrower.club = :club")
	List<Instrument> findAllByClubEquals(@Param("club") Club club);
	
	@Query("SELECT i FROM Instrument i JOIN FETCH i.borrower WHERE i.id = :instrumentId")
	Optional<Instrument> findByInstrumentIdFetchJoinBorrower(@Param("instrumentId") Long instrumentId);
}
