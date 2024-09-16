package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.enums.Role;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
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
	public List<Member> findAllByIdIn(List<Long> memberIds) {
		return memberJpaRepository.findAllByIdIn(memberIds).stream()
				.map(MemberJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public boolean existsByEmail(String email) {
		return memberJpaRepository.existsByEmail(email);
	}
	
	@Override
	public Optional<Member> findByEmail(String email) {
		return memberJpaRepository.findByEmail(email)
				.map(MemberJpaEntity::toDomain);
	}
	
	@Override
	public Member findByIdAndEmail(Long id, String email) {
		return memberJpaRepository.findByIdAndEmail(id, email)
				.orElseThrow(MemberNotFound::new)
				.toDomain();
	}
	
	@Override
	public boolean existsByRole(Role role) {
		return memberJpaRepository.existsByRole(role);
	}
	
	@Override
	public Optional<Member> findById(Long memberId) {
		return memberJpaRepository.findById(memberId)
				.map(MemberJpaEntity::toDomain);
	}
	
	@Override
	public void save(Member member) {
		memberJpaRepository.save(MemberJpaEntity.of(member));
	}
}
