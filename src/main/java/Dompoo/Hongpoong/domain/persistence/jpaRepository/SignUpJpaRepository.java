package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.SignUpJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignUpJpaRepository extends JpaRepository<SignUpJpaEntity, Long> {
    
    boolean existsByEmail(String email);
    
    Optional<SignUpJpaEntity> findByEmail(String email);
}
