package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InstrumentRepositoryImpl implements InstrumentRepository {
	
	private final InstrumentJpaRepository instrumentJpaRepository;
	private final InstrumentBorrowJpaRepository instrumentBorrowJpaRepository;
	
	@Override
	public void save(Instrument instrument) {
		instrumentJpaRepository.save(InstrumentJpaEntity.of(instrument));
	}
	
	@Override
	public void saveBorrow(InstrumentBorrow instrumentBorrow) {
		instrumentBorrowJpaRepository.save(InstrumentBorrowJpaEntity.of(instrumentBorrow));
	}
	
	@Override
	public Optional<Instrument> findById(Long instrumentId) {
		return instrumentJpaRepository.findById(instrumentId)
				.map(InstrumentJpaEntity::toDomain);
	}
	
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
	public Optional<Instrument> findByMemberIdAndInstrumentId(Long memberId, Long instrumentId) {
		return instrumentBorrowJpaRepository.findByMemberIdAndInstrumentId(memberId, instrumentId)
				.map(InstrumentJpaEntity::toDomain);
	}
	
	@Override
	public void delete(Instrument instrument) {
		instrumentJpaRepository.delete(InstrumentJpaEntity.of(instrument));
	}
}
