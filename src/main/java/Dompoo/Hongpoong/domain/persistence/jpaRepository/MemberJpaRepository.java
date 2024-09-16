package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.enums.Role;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {
    List<MemberJpaEntity> findAllByIdIn(List<Long> memberIds);
    boolean existsByEmail(String email);
    Optional<MemberJpaEntity> findByEmail(String email);
    Optional<MemberJpaEntity> findByIdAndEmail(Long id, String email);
    boolean existsByRole(Role role);
}
