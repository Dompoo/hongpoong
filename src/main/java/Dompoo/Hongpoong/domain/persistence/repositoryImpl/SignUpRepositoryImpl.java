package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.domain.jpaEntity.SignUpJpaEntity;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.SignUpJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.SignUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SignUpRepositoryImpl implements SignUpRepository {
	
	private final SignUpJpaRepository signUpJpaRepository;
	
	@Override
	public boolean existsByEmail(String email) {
		return signUpJpaRepository.existsByEmail(email);
	}
	
	@Override
	public Optional<SignUpJpaEntity> findByEmail(String email) {
		return signUpJpaRepository.findByEmail(email);
	}
}
