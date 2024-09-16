package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.jpaEntity.InstrumentJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.InstrumentBorrowJpaEntity;
import Dompoo.Hongpoong.domain.enums.Club;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository {
	
	List<InstrumentJpaEntity> findAllByClubNotEquals(@Param("club") Club club);
	
	List<InstrumentJpaEntity> findAllByClubEquals(@Param("club") Club club);
	
	List<InstrumentBorrowJpaEntity> findAllByInstrument(InstrumentJpaEntity instrumentJpaEntity);
	
	Optional<InstrumentJpaEntity> findByMemberIdAndInstrumentId(Long memberId, Long instrumentId);
}
