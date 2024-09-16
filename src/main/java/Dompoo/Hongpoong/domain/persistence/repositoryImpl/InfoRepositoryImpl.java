package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.common.exception.impl.InfoNotFound;
import Dompoo.Hongpoong.domain.domain.Info;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InfoJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.InfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InfoRepositoryImpl implements InfoRepository {
	
	private final InfoJpaRepository infoJpaRepository;
	
	@Override
	public Info findByIdFetchJoinMember(Long infoId) {
		return infoJpaRepository.findByIdFetchJoinMember(infoId)
				.orElseThrow(InfoNotFound::new)
				.toDomain();
	}
}
