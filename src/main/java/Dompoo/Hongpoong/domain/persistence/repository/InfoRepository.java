package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.jpaEntity.InfoJpaEntity;

import java.util.Optional;

public interface InfoRepository {
	
	Optional<InfoJpaEntity> findByIdFetchJoinMember(Long infoId);
}
