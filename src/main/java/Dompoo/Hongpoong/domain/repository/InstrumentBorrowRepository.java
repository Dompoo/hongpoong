package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.InstrumentBorrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstrumentBorrowRepository extends JpaRepository<InstrumentBorrow, Long> {
	
	@Query("SELECT ib FROM InstrumentBorrow ib WHERE ib.instrument = :instrument")
	List<InstrumentBorrow> findAllByInstrument(@Param("instrument") Instrument instrument);
}
