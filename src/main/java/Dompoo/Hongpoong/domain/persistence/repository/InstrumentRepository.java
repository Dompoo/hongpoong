package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.domain.Instrument;
import Dompoo.Hongpoong.domain.domain.InstrumentBorrow;
import Dompoo.Hongpoong.domain.enums.Club;

import java.util.List;

public interface InstrumentRepository {
	
	List<Instrument> findAllByClubNotEquals(Club club);
	
	List<Instrument> findAllByClubEquals(Club club);
	
	List<InstrumentBorrow> findAllByInstrument(Instrument instrument);
	
	Instrument findByMemberIdAndInstrumentId(Long memberId, Long instrumentId);
}
