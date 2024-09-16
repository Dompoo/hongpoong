package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.SignUp;
import Dompoo.Hongpoong.domain.jpaRepository.SignUpJpaRepository;
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
	public Optional<SignUp> findByEmail(String email) {
		return signUpJpaRepository.findByEmail(email);
	}
}
