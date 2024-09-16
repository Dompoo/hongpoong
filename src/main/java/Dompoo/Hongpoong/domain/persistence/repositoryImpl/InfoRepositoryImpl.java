package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.domain.jpaEntity.InfoJpaEntity;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InfoJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.InfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InfoRepositoryImpl implements InfoRepository {
	
	private final InfoJpaRepository infoJpaRepository;
	
	@Override
	public Optional<InfoJpaEntity> findByIdFetchJoinMember(Long infoId) {
		return infoJpaRepository.findByIdFetchJoinMember(infoId);
	}
}
