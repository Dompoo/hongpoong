package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.jpaEntity.SignUpJpaEntity;

import java.util.Optional;

public interface SignUpRepository {
    
    boolean existsByEmail(String email);
    
    Optional<SignUpJpaEntity> findByEmail(String email);
}
