package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Info;
import Dompoo.Hongpoong.domain.jpaRepository.InfoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InfoRepositoryImpl implements InfoRepository {
	
	private final InfoJpaRepository infoJpaRepository;
	
	@Override
	public Optional<Info> findByIdFetchJoinMember(Long infoId) {
		return infoJpaRepository.findByIdFetchJoinMember(infoId);
	}
}
