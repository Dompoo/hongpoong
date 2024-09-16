package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.domain.Info;

public interface InfoRepository {
	
	Info findByIdFetchJoinMember(Long infoId);
}
