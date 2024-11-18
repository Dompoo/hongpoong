package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.InstrumentBorrow;
import Dompoo.Hongpoong.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InstrumentBorrowRepository extends JpaRepository<InstrumentBorrow, Long> {
	
	@Query("SELECT ib FROM InstrumentBorrow ib WHERE ib.instrument = :instrument")
	List<InstrumentBorrow> findAllByInstrument(@Param("instrument") Instrument instrument);
	
	@Query("SELECT ib.instrument FROM InstrumentBorrow ib WHERE ib.member.id = :memberId AND ib.instrument.id = :instrumentId")
	Optional<Instrument> findByMemberIdAndInstrumentId(Long memberId, Long instrumentId);
	
	List<InstrumentBorrow> findInstrumentBorrowByReservation(Reservation reservation);
}
