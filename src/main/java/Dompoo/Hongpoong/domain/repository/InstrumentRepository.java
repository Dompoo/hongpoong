package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.InstrumentBorrow;
import Dompoo.Hongpoong.domain.enums.Club;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository {
	
	List<Instrument> findAllByClubNotEquals(@Param("club") Club club);
	
	List<Instrument> findAllByClubEquals(@Param("club") Club club);
	
	List<InstrumentBorrow> findAllByInstrument(Instrument instrument);
	
	Optional<Instrument> findByMemberIdAndInstrumentId(Long memberId, Long instrumentId);
}
