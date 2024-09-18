package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.domain.domain.Info;
import Dompoo.Hongpoong.domain.jpaEntity.InfoJpaEntity;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InfoJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.InfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InfoRepositoryImpl implements InfoRepository {
	
	private final InfoJpaRepository infoJpaRepository;
	
	@Override
	public void save(Info info) {
		infoJpaRepository.save(InfoJpaEntity.of(info));
	}
	
	@Override
	public List<Info> findAll() {
		return infoJpaRepository.findAll().stream()
				.map(InfoJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public Optional<Info> findById(Long infoId) {
		return infoJpaRepository.findById(infoId)
				.map(InfoJpaEntity::toDomain);
	}
	
	@Override
	public Optional<Info> findByIdFetchJoinMember(Long infoId) {
		return infoJpaRepository.findByIdFetchJoinMember(infoId)
				.map(InfoJpaEntity::toDomain);
	}
	
	@Override
	public void delete(Info info) {
		infoJpaRepository.delete(InfoJpaEntity.of(info));
	}
}
