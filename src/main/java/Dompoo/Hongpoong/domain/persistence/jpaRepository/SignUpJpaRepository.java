package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.entity.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignUpJpaRepository extends JpaRepository<SignUp, Long> {
    boolean existsByEmail(String email);
    Optional<SignUp> findByEmail(String email);
}
