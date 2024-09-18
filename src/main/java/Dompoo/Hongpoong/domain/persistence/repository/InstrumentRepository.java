package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.domain.Instrument;
import Dompoo.Hongpoong.domain.domain.InstrumentBorrow;
import Dompoo.Hongpoong.domain.enums.Club;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository {
	
	void save(Instrument instrument);
	
	void saveBorrow(InstrumentBorrow instrumentBorrow);
	
	Optional<Instrument> findById(Long instrumentId);
	
	List<Instrument> findAllByClubNotEquals(Club club);
	
	List<Instrument> findAllByClubEquals(Club club);
	
	List<InstrumentBorrow> findAllBorrowByInstrument(Instrument instrument);
	
	Optional<Instrument> findByMemberIdAndInstrumentId(Long memberId, Long instrumentId);
	
	void delete(Instrument instrument);
}
