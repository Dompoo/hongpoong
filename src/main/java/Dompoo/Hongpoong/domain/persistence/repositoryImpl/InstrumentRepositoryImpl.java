package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.domain.jpaEntity.InstrumentJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.InstrumentBorrowJpaEntity;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InstrumentBorrowJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InstrumentJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InstrumentRepositoryImpl implements InstrumentRepository {
	
	private final InstrumentJpaRepository instrumentJpaRepository;
	private final InstrumentBorrowJpaRepository instrumentBorrowJpaRepository;
	
	@Override
	public List<InstrumentJpaEntity> findAllByClubNotEquals(Club club) {
		return instrumentJpaRepository.findAllByClubNotEquals(club);
	}
	
	@Override
	public List<InstrumentJpaEntity> findAllByClubEquals(Club club) {
		return instrumentJpaRepository.findAllByClubEquals(club);
	}
	
	@Override
	public List<InstrumentBorrowJpaEntity> findAllByInstrument(InstrumentJpaEntity instrumentJpaEntity) {
		return instrumentBorrowJpaRepository.findAllByInstrument(instrumentJpaEntity);
	}
	
	@Override
	public Optional<InstrumentJpaEntity> findByMemberIdAndInstrumentId(Long memberId, Long instrumentId) {
		return instrumentBorrowJpaRepository.findByMemberIdAndInstrumentId(memberId, instrumentId);
	}
}
