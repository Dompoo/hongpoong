package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.enums.Role;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    
    List<MemberJpaEntity> findAllByIdIn(List<Long> memberIds);
    
    boolean existsByEmail(String email);
    
    Optional<MemberJpaEntity> findByEmail(String email);
    
    Optional<MemberJpaEntity> findByIdAndEmail(Long id, String email);
    
    boolean existsByRole(Role role);
}
