package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.domain.Info;

import java.util.List;
import java.util.Optional;

public interface InfoRepository {
	
	void save(Info info);
	
	List<Info> findAll();
	
	Optional<Info> findById(Long infoId);
	
	Optional<Info> findByIdFetchJoinMember(Long infoId);
	
	void delete(Info info);
}
