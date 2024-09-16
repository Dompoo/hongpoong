package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.common.exception.impl.SignUpNotFound;
import Dompoo.Hongpoong.domain.domain.SignUp;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.SignUpJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.SignUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SignUpRepositoryImpl implements SignUpRepository {
	
	private final SignUpJpaRepository signUpJpaRepository;
	
	@Override
	public boolean existsByEmail(String email) {
		return signUpJpaRepository.existsByEmail(email);
	}
	
	@Override
	public SignUp findByEmail(String email) {
		return signUpJpaRepository.findByEmail(email)
				.orElseThrow(SignUpNotFound::new)
				.toDomain();
	}
	
}
