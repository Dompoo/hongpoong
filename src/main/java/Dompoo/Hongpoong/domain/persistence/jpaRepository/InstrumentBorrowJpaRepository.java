package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.InstrumentJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.InstrumentBorrowJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InstrumentBorrowJpaRepository extends JpaRepository<InstrumentBorrowJpaEntity, Long> {
	
	@Query("SELECT ib FROM InstrumentBorrowJpaEntity ib WHERE ib.instrument = :instrument")
	List<InstrumentBorrowJpaEntity> findAllByInstrument(@Param("instrument") InstrumentJpaEntity instrumentJpaEntity);
	
	@Query("SELECT ib.instrument FROM InstrumentBorrowJpaEntity ib WHERE ib.member.id = :memberId AND ib.instrument.id = :instrumentId")
	Optional<InstrumentJpaEntity> findByMemberIdAndInstrumentId(Long memberId, Long instrumentId);
}
