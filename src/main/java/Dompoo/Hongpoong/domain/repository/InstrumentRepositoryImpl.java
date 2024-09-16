package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.InstrumentBorrow;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.jpaRepository.InstrumentBorrowJpaRepository;
import Dompoo.Hongpoong.domain.jpaRepository.InstrumentJpaRepository;
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
	public List<Instrument> findAllByClubNotEquals(Club club) {
		return instrumentJpaRepository.findAllByClubNotEquals(club);
	}
	
	@Override
	public List<Instrument> findAllByClubEquals(Club club) {
		return instrumentJpaRepository.findAllByClubEquals(club);
	}
	
	@Override
	public List<InstrumentBorrow> findAllByInstrument(Instrument instrument) {
		return instrumentBorrowJpaRepository.findAllByInstrument(instrument);
	}
	
	@Override
	public Optional<Instrument> findByMemberIdAndInstrumentId(Long memberId, Long instrumentId) {
		return instrumentBorrowJpaRepository.findByMemberIdAndInstrumentId(memberId, instrumentId);
	}
}
