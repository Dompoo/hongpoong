package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.enums.Role;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
	
	private final MemberJpaRepository memberJpaRepository;
	
	@Override
	public List<MemberJpaEntity> findAllByIdIn(List<Long> memberIds) {
		return memberJpaRepository.findAllByIdIn(memberIds);
	}
	
	@Override
	public boolean existsByEmail(String email) {
		return memberJpaRepository.existsByEmail(email);
	}
	
	@Override
	public Optional<MemberJpaEntity> findByEmail(String email) {
		return memberJpaRepository.findByEmail(email);
	}
	
	@Override
	public Optional<MemberJpaEntity> findByIdAndEmail(Long id, String email) {
		return memberJpaRepository.findByIdAndEmail(id, email);
	}
	
	@Override
	public boolean existsByRole(Role role) {
		return memberJpaRepository.existsByRole(role);
	}
}
