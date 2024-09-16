package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Info;

import java.util.Optional;

public interface InfoRepository {
	
	Optional<Info> findByIdFetchJoinMember(Long infoId);
}
