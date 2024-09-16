package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.common.exception.impl.InstrumentNotFound;
import Dompoo.Hongpoong.domain.domain.Instrument;
import Dompoo.Hongpoong.domain.domain.InstrumentBorrow;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.jpaEntity.InstrumentBorrowJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.InstrumentJpaEntity;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InstrumentBorrowJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InstrumentJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InstrumentRepositoryImpl implements InstrumentRepository {
	
	private final InstrumentJpaRepository instrumentJpaRepository;
	private final InstrumentBorrowJpaRepository instrumentBorrowJpaRepository;
	
	@Override
	public List<Instrument> findAllByClubNotEquals(Club club) {
		return instrumentJpaRepository.findAllByClubNotEquals(club).stream()
				.map(InstrumentJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public List<Instrument> findAllByClubEquals(Club club) {
		return instrumentJpaRepository.findAllByClubEquals(club).stream()
				.map(InstrumentJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public List<InstrumentBorrow> findAllBorrowByInstrument(Instrument instrument) {
		return instrumentBorrowJpaRepository.findAllByInstrument(InstrumentJpaEntity.of(instrument)).stream()
				.map(InstrumentBorrowJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public Instrument findByMemberIdAndInstrumentId(Long memberId, Long instrumentId) {
		return instrumentBorrowJpaRepository.findByMemberIdAndInstrumentId(memberId, instrumentId)
				.orElseThrow(InstrumentNotFound::new)
				.toDomain();
	}
}
